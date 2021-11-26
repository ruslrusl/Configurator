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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
    public String generateFile(Jobject jobject) {

        if (jobject != null) {
            String fileName = null;
            if (jobject.getType() == 1) {
                fileName = Util.generateFileNameWithDirectory(Constant.FILE.EXPORT_FILE_MLFB);
            } else if (jobject.getType() == 2) {
                fileName = Util.generateFileNameWithDirectory(Constant.FILE.EXPORT_FILE_MLFB_DESC);
            } else if (jobject.getType() == 3) {
                fileName = Util.generateFileNameWithDirectory(Constant.FILE.EXPORT_FILE_TKP);
            } else if (jobject.getType() == 4) {
                fileName = Util.generateFileNameWithDirectory(Constant.FILE.EXPORT_FILE_MLFB_INDUSTRY);
            } else if (jobject.getType() == 5) {
                fileName = Util.generateFileNameWithDirectory(Constant.FILE.EXPORT_FILE_SPECIFICATION);
            }

            if (fileName != null) {
                if (jobject.getType() == 1 || jobject.getType() == 2 || jobject.getType() == 4) {
                    XSSFWorkbook workbook = new XSSFWorkbook();
                    XSSFSheet sheet = workbook.createSheet();
                    fillExcel(workbook, sheet, jobject.getSensors(), jobject.getType());
                    try {
                        FileOutputStream outputStream = new FileOutputStream(fileName);
                        workbook.write(outputStream);
                        workbook.close();
                        return fileName;
                    } catch (FileNotFoundException e) {
                        log.error("Ошибка при создании файла " + fileName, e);
                    } catch (IOException e) {
                        log.error("Ошибка при создании файла " + fileName, e);
                    }
                } else if (jobject.getType() == 5) {
                    String template = Util.getFileNameSpecification();
                    File source = new File(template);
                    File destination = new File(fileName);
                    try {
                        FileUtils.copyFile(source, destination);
                        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(fileName));
                        XSSFSheet sheet = workbook.getSheetAt(0);
                        fillExcel(workbook, sheet, jobject.getSensors(), jobject.getType());
                        try {
                            FileOutputStream outputStream = new FileOutputStream(fileName);
                            workbook.write(outputStream);
                            workbook.close();
                            return fileName;
                        } catch (FileNotFoundException e) {
                            log.error("Ошибка при создании файла " + fileName, e);
                        } catch (IOException e) {
                            log.error("Ошибка при создании файла " + fileName, e);
                        }
                    } catch (IOException e) {
                        log.error("Ошибка при копировании файла из " + template + " в " + fileName, e);
                    }
                    return fileName;
                }
            }
        }
        return null;
    }

    private void fillExcel(XSSFWorkbook workbook, XSSFSheet sheet, List<Jsensor> jsensorList, int type) {
        int i;
        int rowNum;
        int colNum;
        if (type == 1 || type == 2 || type == 4) {
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
                if (type == 1) {
                    Cell cellSecond = row.createCell(colNum);
                    cellSecond.setCellValue(jsensor.getMlfbrus());
                } else if (type == 2) {
                    Cell cellSecond = row.createCell(colNum);
                    XSSFCellStyle cellStyle3 = getCellStyle(workbook, 3);
                    cellSecond.setCellValue(jsensor.getDescr());
                    cellSecond.setCellStyle(cellStyle3);
                } else if (type == 4) {
                    Cell cellSecond = row.createCell(colNum);
                    cellSecond.setCellValue(jsensor.getMlfb());
                }
            }
            sheet.autoSizeColumn(1);
        } else if (type == 5) {
            i = 0;
            for (Jsensor jsensor : jsensorList) {
                colNum = 1;
                rowNum = i * 7 + 2;
                Row row = sheet.getRow(rowNum++);
                Cell cell1 = row.getCell(colNum++);
                cell1.setCellValue(jsensor.getMlfbrus());
                colNum++;
                Cell cell2 = row.getCell(colNum++);
                cell2.setCellValue(jsensor.getMlfbrus().substring(0, 5));
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
        }
    }

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
