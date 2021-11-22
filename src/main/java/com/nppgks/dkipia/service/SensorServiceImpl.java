package com.nppgks.dkipia.service;

import com.nppgks.dkipia.dao.SensorDAO;
import com.nppgks.dkipia.entity.*;
import com.nppgks.dkipia.util.Constant;
import com.nppgks.dkipia.util.Util;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SensorServiceImpl implements SensorService {

    @Autowired
    private SensorDAO sensorDAO;

    @Override
    public List<SensorTypeEntity> getSensorTypes() {
        return sensorDAO.getSensorTypes();
    }

    @Override
    public List<SelectionEntity> getSelections(SensorTypeEntity sensorTypeEntity, String group, String option, String mlfbCText) {
        List<SelectionEntity> selections = sensorDAO.getSelections(sensorTypeEntity.getId());
        List<SelectOptionEntity> selectOptions = sensorDAO.getSelectionOptions(sensorTypeEntity.getId(), sensorTypeEntity.getMlfb());
        if (selectOptions != null) {
            Map<String, List<SelectOptionEntity>> maps = selectOptions.stream().collect(Collectors.groupingBy(SelectOptionEntity::getGroup));

            for (int i = 0; i < selections.size(); i++) {
                SelectionEntity selection = selections.get(i);
                List<SelectOptionEntity> selectOptionEntities = maps.get(selection.getGroup());
                if (selectOptionEntities != null) {
                    SelectOptionEntity selectOptionEntity = null;
                    if (selectOptionEntities.size() == 1 && selection.isMain()) {
                        //если единственный выбор и основной тип, то выбираем его
                        selectOptionEntity = selectOptionEntities.get(0);
                        setSelection(selection, selectOptionEntity);
                    } else {
                        //иначе проверка на то, был ли до этого выбран
                        if (selection.getGroup() != null && sensorTypeEntity.getMlfb() != null && !sensorTypeEntity.getMlfb().isEmpty()) {
                            if (selection.isMain()) {//основные типы были выбраны
                                int j = Integer.valueOf(selection.getGroup()) - 1;
                                String curGroupValue = sensorTypeEntity.getMlfb().substring(j, j + 1);
                                selectOptionEntity = selectOptionEntities.stream()
                                        .filter(p -> p.getName().equalsIgnoreCase(curGroupValue))
                                        .findFirst().orElse(null);

                                setSelection(selection, selectOptionEntity);
                            } else {//проходим по опциям
                                //сперва устанавливаем выбранных по B-строке
                                if (sensorTypeEntity.getMlfbB() != null) {
                                    selectOptionEntities.stream()
                                            //.filter(p -> !p.getGroup().equalsIgnoreCase(group))
                                            .forEach(s -> {
                                                if (sensorTypeEntity.getMlfbB().contains(s.getName())) {
//                                                    System.out.println("setSelection");
                                                    setSelection(selection, s);
                                                }
                                            });
                                }
                                //проходимся по текущей нажатой опции
                                if (!option.isEmpty() && !option.equalsIgnoreCase("0")) {
                                    selectOptionEntities.stream()
                                            .filter(p -> p.getGroup().equalsIgnoreCase(group))//берем все данные по этой группе
                                            .forEach(s -> {
                                                if (s.getName().equalsIgnoreCase(option)) {//устанавливаем выбранным и данные вставляем
                                                    s.setSelected(true);
                                                    sensorTypeEntity.setOneMlfbB(option);
                                                    if (mlfbCText != null) {
                                                        sensorTypeEntity.setOneMlfbC(option, mlfbCText);
                                                        insertOptionParams(s, option, mlfbCText);
                                                    }
                                                    setSelection(selection, s);
                                                } else {//убираем выбор
                                                    s.setSelected(false);
                                                    sensorTypeEntity.removeOneMlfbB(s.getName());
                                                    sensorTypeEntity.removeOneMlfbC(s.getName());
                                                }
                                            });
                                }
                                if (sensorTypeEntity.getMlfbC() != null) {
                                    //здесь устанавливаю Options для всех групп по Y
                                    selectOptionEntities.stream()
                                            .filter(p -> p.getGroup().equalsIgnoreCase(group))
                                            .forEach(s -> {

                                                Optional<String> optional = sensorTypeEntity.getMlfbC().stream().filter(x -> x.contains(s.getName() + ":")).findFirst();
                                                if (optional.isPresent()) {
                                                    insertOptionParams(s, s.getName(), optional.orElse(""));
                                                }
                                            });
                                }
                            }
                        }
                    }
                    //добавляем главные опции
                    selection.setSelectOption(selectOptionEntities);
                }
                selection.setDisplay(selection.getGroup().equalsIgnoreCase(group));
            }
        }
        return selections;
    }


    private void insertOptionParams(SelectOptionEntity selectOptionEntity, String currentMlfbB, String mlfbCText) {
        if (selectOptionEntity != null && mlfbCText != null && currentMlfbB != null && !mlfbCText.isEmpty() && !currentMlfbB.isEmpty()) {
            String strWithoutCaption = mlfbCText.replace(currentMlfbB + ":", "").trim();
            if (currentMlfbB.equalsIgnoreCase("Y15") || currentMlfbB.equalsIgnoreCase("Y16") || currentMlfbB.equalsIgnoreCase("Y21")) {
                OptionParam optionParam = new OptionParam();
                optionParam.setParam1(strWithoutCaption);
                optionParam.setValue(mlfbCText);
                selectOptionEntity.setOptionParam(optionParam);
            } else if (currentMlfbB.equalsIgnoreCase("Y01") || currentMlfbB.equalsIgnoreCase("Y02") || currentMlfbB.equalsIgnoreCase("Y22")) {
                OptionParam optionParam = new OptionParam();
                optionParam.setValue(mlfbCText);
                String str[] = strWithoutCaption.split("\\.\\.\\.");
                if (str != null && str.length == 2) {
                    optionParam.setParam1(str[0].trim());
                    String[] str2 = str[1].trim().split("\\s+");
                    optionParam.setParam2(str2[0].trim());
                    optionParam.setParam3(str2[1].trim());
                }
                selectOptionEntity.setOptionParam(optionParam);
            }
        }
    }
//
//    private void insertOptionParams(SelectOptionEntity selectOptionEntity, String oneMlfbB, String[] mlfbCArr) {
//        if (selectOptionEntity != null) {
////            String[] mlfbCArr = Util.getMlfbCString(mlfbC);
//            String oneMlfbC = null;
//            for (String tempC : mlfbCArr) {
//                if (tempC.matches(".*" + oneMlfbB + ".*")) {
//                    oneMlfbC = tempC.replace(oneMlfbB + ":", "").trim();
//                    break;
//                }
//            }
//            if (oneMlfbC != null) {
//                if (oneMlfbB.equalsIgnoreCase("Y15") || oneMlfbB.equalsIgnoreCase("Y16") || oneMlfbB.equalsIgnoreCase("Y21")) {
//                    OptionParam optionParam = new OptionParam();
//                    optionParam.setParam1(oneMlfbC);
//                    optionParam.setValue(oneMlfbC);
//                    selectOptionEntity.setOptionParam(optionParam);
//                } else if (oneMlfbB.equalsIgnoreCase("Y01") || oneMlfbB.equalsIgnoreCase("Y02") || oneMlfbB.equalsIgnoreCase("Y22")) {
//                    OptionParam optionParam = new OptionParam();
//                    optionParam.setValue(oneMlfbC);
//                    String str[] = oneMlfbC.split("\\.\\.\\.");
//                    if (str != null && str.length == 2) {
//                        optionParam.setParam1(str[0].trim());
//                        String[] str2 = str[1].trim().split("\\s+");
//                        optionParam.setParam2(str2[0].trim());
//                        optionParam.setParam3(str2[1].trim());
//                    }
//                    selectOptionEntity.setOptionParam(optionParam);
//                }
//            }
//        }
//    }

    private void setSelection(SelectionEntity selection, SelectOptionEntity selectOptionEntity) {
        if (selectOptionEntity != null) {
            selectOptionEntity.setSelected(true);
            selection.setCurrentSelect(selectOptionEntity);
            selection.setSelectType(1);//TODO пока выбрано правильно
        }
    }

//    @Override
//    public String[] getMlfbSeparate(String mlfb) {
//        String[] rez = new String[11];
//        int j;
//        if (mlfb != null) {
//            String mlfbPart = mlfb.substring(5);
//            for (int i = 0; i < rez.length; i++) {
//                j = i + 1;
//                try {
//                    rez[i] = mlfbPart.substring(i, j);
//                } catch (IndexOutOfBoundsException ex) {
//                    rez[i] = Constant.MLFB.MLFB_EMPTY;
//                }
//            }
//        } else {
//            for (int i = 0; i < rez.length; i++) {
//                rez[i] = Constant.MLFB.MLFB_EMPTY;
//            }
//        }
//        return rez;
//    }

    @Override
    public SensorTypeEntity getSensorTypeById(int id) {
        List<SensorTypeEntity> sensorTypeEntities = sensorDAO.getSensorTypes();
        if (sensorTypeEntities != null) {
            SensorTypeEntity sensorTypeEntity = sensorTypeEntities.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
            return sensorTypeEntity;
        }
        return null;
    }

    @Override
    public List<Sensors> getSensors() {
        return sensorDAO.getSensors();
    }

    @Override
    public Sensors getSensorById(int idSensor) {
        return sensorDAO.getSensorById(idSensor);
    }

    @Override
    public List<SensorsLabels> getSensorLabels(int idSensor) {
        List<SensorsLabels> sensorsLabels = sensorDAO.getSensorLabels(idSensor);
        return sensorsLabels;
    }

    @Override
    public void setActiveLabelByPosition(List<SensorsLabels> sensorsLabels, String position) {
        sensorsLabels.stream()
                .filter(s -> s.getPosition().equals(position))
                .forEach(s ->
                {
                    s.setActive(true);
                    List<SensorsOptionNames> sensorsOptionNames = sensorDAO.getSensorOptions(s.getId());
                    s.setSensorsOptionNames(sensorsOptionNames);
                });
    }

    @Override
    public void setActiveLabelByPosition(List<SensorsLabels> sensorsLabels, String position, String mlfb) {
        sensorsLabels.stream()
                .filter(s -> s.getPosition().equals(position))
                .forEach(s ->
                {
                    s.setActive(true);
//                    System.out.println("sensorsLabels.getId() = " + s.getId());
                    List<SensorsOptionNames> sensorsOptionNames = sensorDAO.getSensorOptionsByRule(mlfb, s.getId());
                    s.setSensorsOptionNames(sensorsOptionNames);
                });
    }

    @Override
    public void setSelectedPositionsByMlfb(List<SensorsLabels> sensorsLabels, String mlfb) {

        List<SensorsOptionNames> sensorsOptionNamesList = sensorDAO.getSensorOptionsByMlfb(mlfb);
        sensorsOptionNamesList.stream().forEach(o ->
        {
            sensorsLabels.stream()
                    .filter(s -> s.getPosition().equals(o.getGroup()))
                    .forEach(s -> {
                        s.setCurrentOptionSelect(o);
                        s.setSelectType(Constant.STATUS.OK);
                        if (s.getSensorsOptionNames() != null)
                            s.getSensorsOptionNames().stream()
                                    .filter(on -> on.getId() == o.getId())
                                    .forEach(on -> on.setSelected(true));
                    });
        });
        sensorsLabels.stream()
                .forEach(s -> s.getSensorsOptionNames());
    }


    @Override
    public void setSelectedPositionsByMlfbB(List<SensorsLabels> sensorsLabels, int idSensor, String mlfbB) {

        List<SensorsOptionNames> sensorsOptionNamesList = sensorDAO.getSensorOptionsByMlfbB(idSensor, mlfbB);
        sensorsOptionNamesList.stream().forEach(o ->
        {
            sensorsLabels.stream()
                    .filter(s -> s.getPosition().equals(o.getGroup()))
                    .forEach(s -> {
                        s.setCurrentOptionSelect(o);
                        s.setSelectType(Constant.STATUS.OK);
                        if (s.getSensorsOptionNames() != null)
                            s.getSensorsOptionNames().stream()
                                    .filter(on -> on.getId() == o.getId())
                                    .forEach(on -> on.setSelected(true));
                    });
        });
        sensorsLabels.stream()
                .forEach(s -> s.getSensorsOptionNames());
    }

    @Override
    public void setYoptions(List<SensorsLabels> sensorsLabels, String mlfbC) {
//        System.out.println("setYoptions = " + mlfbC);
        List<String> stringList = Util.separateString(mlfbC, 3);
        stringList.forEach(y ->
                {
//                    System.out.println(y);
                    //устанавливаем для текущего
                    sensorsLabels.stream()
                            .filter(sl -> sl.getCurrentOptionSelect() != null)
                            .filter(sl ->
                                    y.startsWith(sl.getCurrentOptionSelect().getOption())
                            )
                            .forEach(sl ->
                                    {
                                        sl.getCurrentOptionSelect().setOptionParam(new OptionParam(y));
                                    }
                            );
                    //также устанавливаем для всех возможных
                    sensorsLabels.stream()
                            .filter(sl -> sl.getSensorsOptionNames() != null)
                            .flatMap(sl -> sl.getSensorsOptionNames().stream())
                            .filter(sl ->
                                    y.startsWith(sl.getOption())
                            )
                            .forEach(sl ->
                                    {
                                        sl.setOptionParam(new OptionParam(y));
                                    }
                            );
                }
        );
    }

    @Override
    public String[] getMlfbBAndMlfbC(List<SensorsLabels> sensorsLabels, String mlfbB, String mlfbC, String group, String option, String mlfbCText) {
        String[] result = {mlfbB, mlfbC};
        if (group != null && !NumberUtils.isCreatable(group)) {
            if (option != null && !option.equals("0")) {
                List<String> listMlfbс = Util.separateString(mlfbC, 3);

                if (mlfbB != null && !mlfbB.isEmpty()) {
                    List<String> listMlfbb = Util.separateString(mlfbB, 1);
//                    System.out.println("listMlfbb before (" + group + ") " + listMlfbb.toString());
                    sensorsLabels.stream()
                            .filter(o -> o.getPosition().equals(group))
                            .filter(o -> o.getSensorsOptionNames() != null)
                            .filter(o -> o.getEltype() == Constant.SENSOR.ELEMENT_RADIO)
                            .forEach(
                                    o -> o.getSensorsOptionNames().stream().forEach(opt ->
                                            {
                                                listMlfbb.remove(opt.getOption());
                                                listMlfbс.removeIf(c -> c.startsWith(opt.getOption()));
                                            }
                                    )
                            );
                    listMlfbb.add(option);
                    result[0] = Util.concatenateString(listMlfbb, 1);
                } else {
                    result[0] = option;
                }

//                System.out.println("mlfbCText = " + mlfbCText);
//                System.out.println("listMlfbс");
//                System.out.println(listMlfbс);
                if (mlfbCText != null && !mlfbCText.isEmpty()) {
                    listMlfbс.add(mlfbCText);
                    result[1] = Util.concatenateString(listMlfbс, 3);
                }
            }
        }
        return result;
    }

    @Override
    public List<SensorStatus> getSensorStatus(String mlfb) {
        return sensorDAO.getSensorStatus(mlfb);
    }

    @Override
    public void setSelectTypeByStatus(List<SensorsLabels> sensorsLabels, List<SensorStatus> sensorStatus) {
        if (sensorStatus != null) {
            for (SensorStatus status : sensorStatus) {
                //if (status.getStatus()==Constant.STATUS.ERROR) {
//                    String find = Util.getRuleValue(status.getRule1(), true);
//                    if (!find.isEmpty()) {
//                        sensorsLabels.stream()
//                                .filter(s->s.getPosition().equals(find))
//                                .forEach(s->s.setSelectType(status.getStatus()));
//                    }
                setSelectTypeForSensorsLabels(sensorsLabels, status.getRule1(), status.getStatus());
                setSelectTypeForSensorsLabels(sensorsLabels, status.getRule2(), status.getStatus());
                setSelectTypeForSensorsLabels(sensorsLabels, status.getRule3(), status.getStatus());
                setSelectTypeForSensorsLabels(sensorsLabels, status.getRule4(), status.getStatus());
                //}
            }
        }
    }

    @Override
    public String getRussianMlfb(String mlfb) {
        return sensorDAO.getRussianMlfb(mlfb);
    }

    private void setSelectTypeForSensorsLabels(List<SensorsLabels> sensorsLabels, String rule, int status) {
        String find = Util.getRuleValue(rule, true);
        if (!find.isEmpty()) {
            sensorsLabels.stream()
                    .filter(s->s.getPosition().equals(find))
                    .forEach(s->s.setSelectType(status));
        }
    }

    @Override
    public void setMlfbWithOptions(Sensors sensor, String mlfb, String mlfbB, String mlfbC) {
        sensor.setMlfb(mlfb);
        sensor.setMlfbB(mlfbB);
        sensor.setMlfbC(mlfbC);
    }


//    @Override
//    public void setActiveLabelById(List<SensorsLabels> sensorsLabels, int idLabel) {
//        sensorsLabels.stream()
//                .filter(s -> Integer.valueOf(s.getId()).equals(idLabel))
//                .forEach(s ->
//                        {
//                            s.setActive(true);
//                            List<SensorsOptionNames> sensorsOptionNames = sensorDAO.getSensorOptions(s.getId());
//                            s.setSensorsOptionNames(sensorsOptionNames);
//                        }
//                );
//    }

}
