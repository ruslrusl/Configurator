package com.nppgks.dkipia.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nppgks.dkipia.entity.outside.Jcomplete;
import com.nppgks.dkipia.entity.outside.Jobject;
import com.nppgks.dkipia.entity.outside.Jsensor;
import com.nppgks.dkipia.util.Constant;
import com.nppgks.dkipia.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Slf4j
@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public Jobject convertFromJson(String json) {
        if (json != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(json, Jobject.class);
            } catch (JsonProcessingException e) {
                log.error("Ошибка при парсинге json", e);
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public String generateFile(List<Jsensor> jsensorList, int type, int number) {
        if (jsensorList != null) {
            String fileName = null;
            if (type == Constant.FILE.EXPORT_TYPE_MLFB) {
                fileName = Util.generateFileNameWithDirectory(Constant.FILE.EXPORT_FILE_MLFB, number);
            } else if (type == Constant.FILE.EXPORT_TYPE_MLFB_DESC) {
                fileName = Util.generateFileNameWithDirectory(Constant.FILE.EXPORT_FILE_MLFB_DESC, number);
            } else if (type == Constant.FILE.EXPORT_TYPE_TKP) {
                fileName = Util.generateFileNameWithDirectory(Constant.FILE.EXPORT_FILE_TKP, number);
            } else if (type == Constant.FILE.EXPORT_TYPE_MLFB_INDUSTRY) {
                fileName = Util.generateFileNameWithDirectory(Constant.FILE.EXPORT_FILE_MLFB_INDUSTRY, number);
            } else if (type == Constant.FILE.EXPORT_TYPE_SPECIFICATION) {
                fileName = Util.generateFileNameWithDirectory(Constant.FILE.EXPORT_FILE_SPECIFICATION, number);
            }

            if (fileName != null) {
                XSSFWorkbook workbook;
                XSSFSheet sheet;
                if (type == Constant.FILE.EXPORT_TYPE_SPECIFICATION || type == Constant.FILE.EXPORT_TYPE_TKP) {
                    String template = Util.getFileNameFromTemplate(type);
                    File source = new File(template);
                    File destination = new File(fileName);
                    try {
                        FileUtils.copyFile(source, destination);
                        workbook = new XSSFWorkbook(new FileInputStream(fileName));
                        sheet = workbook.getSheetAt(0);
                    } catch (IOException e) {
                        log.error("Ошибка при копировании файла из " + template + " в " + fileName, e);
                        return null;
                    }
                } else {
                    workbook = new XSSFWorkbook();
                    sheet = workbook.createSheet();
                }
                fillExcel(workbook, sheet, jsensorList, type);
                try {
                    FileOutputStream outputStream = new FileOutputStream(fileName);
                    workbook.write(outputStream);
                    workbook.close();
                    return fileName;
                } catch (IOException e) {
                    log.error("Ошибка при создании файла " + fileName, e);
                }
            }
        }
        return null;
    }

    /**
     * Заполнение excel файла
     *
     * @param workbook    рабочая книга Excel
     * @param sheet       лист Excel
     * @param jsensorList список для заполнения
     * @param type        тип выгрузки
     */
    private void fillExcel(XSSFWorkbook workbook, XSSFSheet sheet, List<Jsensor> jsensorList, int type) {
        int i;
        int rowNum;
        int colNum;
        if (type == Constant.FILE.EXPORT_TYPE_MLFB || type == Constant.FILE.EXPORT_TYPE_MLFB_DESC || type == Constant.FILE.EXPORT_TYPE_MLFB_INDUSTRY) {
            rowNum = 0;
            colNum = 0;
            XSSFCellStyle cellStyle = getCellStyle(workbook, 1);
            XSSFCellStyle cellStyle2 = getCellStyle(workbook, 2);

            Row rowName = sheet.createRow(rowNum++);
            Cell cellNameNumb = rowName.createCell(colNum++);
            cellNameNumb.setCellValue("№");
            cellNameNumb.setCellStyle(cellStyle);
            Cell cellName = rowName.createCell(colNum);
            cellName.setCellValue("Наименование");
            cellName.setCellStyle(cellStyle);

            for (Jsensor jsensor : jsensorList) {
                colNum = 0;
                Row row = sheet.createRow(rowNum++);
                Cell cellNumb = row.createCell(colNum++);
                cellNumb.setCellValue(jsensor.getNumber());
                cellNumb.setCellStyle(cellStyle2);
                if (type == Constant.FILE.EXPORT_TYPE_MLFB) {
                    Cell cellSecond = row.createCell(colNum);
                    cellSecond.setCellValue(jsensor.getMlfbrus());
                } else if (type == Constant.FILE.EXPORT_TYPE_MLFB_DESC) {
                    Cell cellSecond = row.createCell(colNum);
                    XSSFCellStyle cellStyle3 = getCellStyle(workbook, 3);
                    cellSecond.setCellValue(jsensor.getDescr());
                    cellSecond.setCellStyle(cellStyle3);
                } else {
                    Cell cellSecond = row.createCell(colNum);
                    cellSecond.setCellValue(jsensor.getMlfb());
                }
            }
            sheet.autoSizeColumn(1);
        } else if (type == Constant.FILE.EXPORT_TYPE_SPECIFICATION) {
            i = 0;
            for (Jsensor jsensor : jsensorList) {
                colNum = 1;
                rowNum = i * 7 + 2;
                Row row = sheet.getRow(rowNum++);
                Cell cell1 = row.getCell(colNum++);
                cell1.setCellValue(jsensor.getMlfbrus());
                colNum++;
                Cell cell2 = row.getCell(colNum++);
                cell2.setCellValue(jsensor.getMlfbrus().substring(0, 6));
                Cell cell3 = row.getCell(colNum++);
                cell3.setCellValue("НПП \"ГКС\"");
                colNum++;
                Cell cell4 = row.getCell(colNum);
                cell4.setCellValue(jsensor.getCount());
                if (jsensor.getComplete() != null) {
                    for (Jcomplete jcomplete : jsensor.getComplete()) {
                        int colNumcomplete = 2;
                        Row rowComplete = sheet.getRow(rowNum++);
                        Cell cell1Complete = rowComplete.getCell(colNumcomplete++);
                        cell1Complete.setCellValue(jcomplete.getDescr());

                        Cell cell2Complete = rowComplete.getCell(colNumcomplete++);
                        cell2Complete.setCellValue(jcomplete.getName());

                        Cell cell3Complete = rowComplete.getCell(colNumcomplete++);
                        cell3Complete.setCellValue(jcomplete.getProvider());

                        colNumcomplete++;
                        Cell cell4Complete = rowComplete.getCell(colNumcomplete);
                        cell4Complete.setCellValue(jcomplete.getCount());
                    }
                }
                i++;
            }
        } else if (type == Constant.FILE.EXPORT_TYPE_TKP) {
            i = 0;
            int startFill = 33;
            int endFill = startFill+20;
            int rowHeight = 7000;
            for (Jsensor jsensor : jsensorList) {
                colNum = 1;
                rowNum = startFill + i;
                sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 9));
                Row row = sheet.getRow(rowNum);
                Cell cell1 = row.getCell(colNum++);
                cell1.setCellValue(jsensor.getNumber());
                StringBuilder descr = new StringBuilder(jsensor.getDescr());
                if (jsensor.getComplete() != null) {
                    for (Jcomplete jcomplete : jsensor.getComplete()) {
                        descr.append("\n").append(jcomplete.getDescr());
                    }
                }
                Cell cell2 = row.getCell(colNum);
                cell2.setCellValue(descr.toString());
                colNum = colNum + 8;
                Cell cell3 = row.getCell(colNum++);
                cell3.setCellValue(Double.parseDouble(jsensor.getTotalpricecoef()));
                Cell cell4 = row.getCell(colNum++);
                cell4.setCellValue(jsensor.getCount());
                Cell cell5 = row.getCell(colNum);
                cell5.setCellValue(Double.parseDouble(jsensor.getTotalpricetotal()));

                row.setHeight((short) rowHeight);
                i++;
            }
            //удаление ненужных строк до 53 строки
            int del = startFill+i;
            int tempdel = del;
            if (del<endFill) {
                while (del<endFill) {
                    removeRow(sheet, tempdel);
                    del++;
                }
            }
            XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
        }
    }

    /**
     * Remove a row by its index
     * @param sheet a Excel sheet
     * @param rowIndex a 0 based index of removing row
     */
    public static void removeRow(XSSFSheet sheet, int rowIndex) {
        int lastRowNum=sheet.getLastRowNum();
        if(rowIndex>=0&&rowIndex<lastRowNum){
            sheet.shiftRows(rowIndex+1,lastRowNum, -1, false, false);
        }
        if(rowIndex==lastRowNum){
            XSSFRow removingRow=sheet.getRow(rowIndex);
            if(removingRow!=null){
                sheet.removeRow(removingRow);
            }
        }
    }

    /**
     * Получение стиля
     *
     * @param workbook рабочая книга Excel
     * @param type     тип стиля
     * @return стиль
     */
    private XSSFCellStyle getCellStyle(XSSFWorkbook workbook, int type) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        if (type == 1) {
            XSSFFont font = workbook.createFont();
            font.setBold(true);
            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
        } else if (type == 2) {
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        } else if (type == 3) {
            cellStyle.setWrapText(true);
        }
        return cellStyle;
    }
}
