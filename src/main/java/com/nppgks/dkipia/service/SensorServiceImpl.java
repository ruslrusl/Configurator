package com.nppgks.dkipia.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nppgks.dkipia.dao.SensorDAO;
import com.nppgks.dkipia.entity.*;
import com.nppgks.dkipia.util.Constant;
import com.nppgks.dkipia.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SensorServiceImpl implements SensorService {

    @Autowired
    private SensorDAO sensorDAO;

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
        return sensorDAO.getSensorLabels(idSensor);
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
                    List<SensorsOptionNames> sensorsOptionNames = sensorDAO.getSensorOptionsByRule(mlfb, s.getId());
                    s.setSensorsOptionNames(sensorsOptionNames);
                });
    }

    @Override
    public void setSelectedPositionsByMlfb(List<SensorsLabels> sensorsLabels, String mlfb) {

        List<SensorsOptionNames> sensorsOptionNamesList = sensorDAO.getSensorOptionsByMlfb(mlfb);
        sensorsOptionNamesList.forEach(o ->
                sensorsLabels.stream()
                        .filter(s -> s.getPosition().equals(o.getGroup()))
                        .forEach(s -> {
                            s.setCurrentOptionSelect(o);
                            s.setSelectType(Constant.STATUS.OK);
                            if (s.getSensorsOptionNames() != null)
                                s.getSensorsOptionNames().stream()
                                        .filter(on -> on.getId() == o.getId())
                                        .forEach(on -> on.setSelected(true));
                        })
        );
        sensorsLabels.forEach(SensorsLabels::getSensorsOptionNames);
    }


    @Override
    public void setSelectedPositionsByMlfbB(List<SensorsLabels> sensorsLabels, int idSensor, String mlfbB) {

        List<SensorsOptionNames> sensorsOptionNamesList = sensorDAO.getSensorOptionsByMlfbB(idSensor, mlfbB);
        sensorsOptionNamesList.forEach(o ->
                sensorsLabels.stream()
                        .filter(s -> s.getPosition().equals(o.getGroup()))
                        .forEach(s -> {
                            s.setCurrentOptionSelect(o);
                            s.setSelectType(Constant.STATUS.OK);
                            if (s.getSensorsOptionNames() != null)
                                s.getSensorsOptionNames().stream()
                                        .filter(on -> on.getId() == o.getId())
                                        .forEach(on -> on.setSelected(true));
                        }));
        sensorsLabels.forEach(SensorsLabels::getSensorsOptionNames);
    }

    @Override
    public void setYoptions(List<SensorsLabels> sensorsLabels, String mlfbC) {
        List<String> stringList = Util.separateString(mlfbC, 3);
        stringList.forEach(y ->
                {
                    //устанавливаем для текущего
                    sensorsLabels.stream()
                            .filter(sl -> sl.getCurrentOptionSelect() != null)
                            .filter(sl -> y.startsWith(sl.getCurrentOptionSelect().getOption()))
                            .forEach(sl -> sl.getCurrentOptionSelect().setOptionParam(new OptionParam(y)));
                    //также устанавливаем для всех возможных
                    sensorsLabels.stream()
                            .filter(sl -> sl.getSensorsOptionNames() != null)
                            .flatMap(sl -> sl.getSensorsOptionNames().stream())
                            .filter(sl -> y.startsWith(sl.getOption()))
                            .forEach(sl -> sl.setOptionParam(new OptionParam(y)));
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
                    sensorsLabels.stream()
                            .filter(o -> o.getPosition().equals(group))
                            .filter(o -> o.getSensorsOptionNames() != null)
                            .filter(o -> o.getEltype() == Constant.SENSOR.ELEMENT_RADIO)
                            .forEach(
                                    o -> o.getSensorsOptionNames().forEach(opt ->
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
                setSelectTypeForSensorsLabels(sensorsLabels, status.getRule1(), status.getStatus());
                setSelectTypeForSensorsLabels(sensorsLabels, status.getRule2(), status.getStatus());
                setSelectTypeForSensorsLabels(sensorsLabels, status.getRule3(), status.getStatus());
                setSelectTypeForSensorsLabels(sensorsLabels, status.getRule4(), status.getStatus());
            }
        }
    }

    @Override
    public String getRussianMlfb(String mlfb) {
        return sensorDAO.getRussianMlfb(mlfb);
    }

    @Override
    public List<String> getSeparateMlfb(String mlfb) {
        return sensorDAO.getSeparateMlfb(mlfb);
    }

    @Override
    public List<Complete> getComplete(boolean isused) {
        List<Complete> list = sensorDAO.getComplete();
        if (isused) {
            return list.stream().filter(s -> s.getIsused() == 1).collect(Collectors.toList());
        } else {
            return list;
        }
    }

    @Override
    public SensorFull getSensorFull(String mlfb) {
        return sensorDAO.getSensorFull(mlfb);
    }

    @Override
    public List<SensorFull> getSensorFullList(String mlfb) {
        SensorFull sensorFull = sensorDAO.getSensorFull(mlfb);
        List<SensorFull> list = new ArrayList<>();
        list.add(sensorFull);
        return list;
    }

    @Override
    public List<SensorFull> getSensorFullList(List<String> mlfbList) {
        List<SensorFull> list = new ArrayList<>();
        if (mlfbList != null) {
            for (String mlfb : mlfbList) {
                SensorFull sensorFull = sensorDAO.getSensorFull(mlfb);
                list.add(sensorFull);
            }
        }
        return list;
    }

    private void setSelectTypeForSensorsLabels(List<SensorsLabels> sensorsLabels, String rule, int status) {
        String find = Util.getRuleValue(rule, true);
        if (!find.isEmpty()) {
            sensorsLabels.stream()
                    .filter(s -> s.getPosition().equals(find))
                    .forEach(s -> s.setSelectType(status));
        }
    }

    @Override
    public void setMlfbWithOptions(Sensors sensor, String mlfb, String mlfbB, String mlfbC) {
        sensor.setMlfb(mlfb);
        sensor.setMlfbB(mlfbB);
        sensor.setMlfbC(mlfbC);
    }

    @Override
    public <T> List<T> convertFromJsonToObject(Class<T> tClass, String json) {
        if (json != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(json, new TypeReference<List<T>>() {
                });
            } catch (JsonProcessingException e) {
                log.error("Ошибка при парсинге", e);
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean saveComplete(List<Complete> completeList) {

        List<List<? extends Object>> list = new ArrayList<>();
        list.add(completeList.stream().map(Complete::getId).collect(Collectors.toList()));
        list.add(completeList.stream().map(Complete::getName).collect(Collectors.toList()));
        list.add(completeList.stream().map(Complete::getPrice).collect(Collectors.toList()));
        list.add(completeList.stream().map(Complete::getCoef).collect(Collectors.toList()));
        list.add(completeList.stream().map(Complete::getOrdernumb).collect(Collectors.toList()));
        list.add(completeList.stream().map(Complete::getDescr).collect(Collectors.toList()));
        list.add(completeList.stream().map(Complete::getProvider).collect(Collectors.toList()));
        list.add(completeList.stream().map(Complete::getUnit).collect(Collectors.toList()));
        return sensorDAO.saveComplete(list);
    }

    @Override
    public List<Price> getPrice(int idSensor) {
        return sensorDAO.getPrice(idSensor);
    }

    @Override
    public boolean savePrice(List<Price> priceList) {
        List<List<? extends Object>> list = new ArrayList<>();
        list.add(priceList.stream().map(Price::getId).collect(Collectors.toList()));
        list.add(priceList.stream().map(Price::getPrice).collect(Collectors.toList()));
        return sensorDAO.savePrice(list);
    }


}
