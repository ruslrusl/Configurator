package com.nppgks.dkipia.dao;

import com.nppgks.dkipia.entity.*;
import com.nppgks.dkipia.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class SensorDAOImpl implements SensorDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<SensorTypeEntity> getSensorTypes() {
        List<SensorTypeEntity> sensorTypeEntityList = callFunction(SensorTypeEntity.class, "configurator$selectdevice()", null);
        return sensorTypeEntityList;
    }

    @Override
    public List<SelectionEntity> getSelections(int idSensor) {
        Object[] inParamArr = {idSensor};
        List<SelectionEntity> selections = callFunction(SelectionEntity.class, "configurator$selectgroupname(?)", inParamArr);
        return selections;
    }

    @Override
    public List<SelectOptionEntity> getSelectionOptions(int idSensor, String mlfb) {
        Object[] inParamArr = {idSensor, mlfb};
        System.out.println("idSensor = ["+idSensor+"], mlfb = ["+mlfb+"]");
        List<SelectOptionEntity> selectOptions = callFunction(SelectOptionEntity.class, "configurator$selectgroups(?,?)", inParamArr);
        return selectOptions;
    }

    @Override
    public List<Sensors> getSensors() {
        List<Sensors> sensorList = callFunction(Sensors.class, "configurator$selectsensors()", null);
        System.out.println(sensorList);
        return sensorList;
    }

    @Override
    public Sensors getSensorById(int idSensor) {
        Object[] inParamArr = {idSensor};
        List<Sensors> sensorList = callFunction(Sensors.class, "configurator$selectsensorsbyid(?)", inParamArr);
        if (sensorList!=null) {
            return sensorList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<SensorsLabels> getSensorLabels(int idSensor) {
        Object[] inParamArr = {idSensor};
        List<SensorsLabels> selections = callFunction(SensorsLabels.class, "configurator$selectlabels(?)", inParamArr);
        return selections;
    }

    @Override
    public List<SensorsOptionNames> getSensorOptions(int idSensorLabels) {
        Object[] inParamArr = {idSensorLabels};
        List<SensorsOptionNames> selections = callFunction(SensorsOptionNames.class, "configurator$selectoptions(?)", inParamArr);
        return selections;
    }

    @Override
    public List<SensorsOptionNames> getSensorOptionsByRule(String mlfb, int idSensorLabels) {
        Object[] inParamArr = {mlfb, idSensorLabels};
        List<SensorsOptionNames> selections = callFunction(SensorsOptionNames.class, "configurator$selectoptionsbyrule(?, ?)", inParamArr);
        return selections;
    }

    @Override
    public List<SensorsOptionNames> getSensorOptionsByMlfb(String mlfb) {
        Object[] inParamArr = {mlfb};
        List<SensorsOptionNames> selections = callFunction(SensorsOptionNames.class, "configurator$getselectedoptionsbymlfb(?)", inParamArr);
        return selections;
    }

    @Override
    public List<SensorsOptionNames> getSensorOptionsByMlfbB(int idSensor, String mlfbB) {
        Object[] inParamArr = {idSensor, mlfbB};
        List<SensorsOptionNames> selections = callFunction(SensorsOptionNames.class, "configurator$getselectedoptionsbyb(?, ?)", inParamArr);
        return selections;
    }

    @Override
    public List<SensorStatus> getSensorStatus(String mlfb) {
        Object[] inParamArr = {mlfb};
        List<SensorStatus> selections = callFunction(SensorStatus.class, "configurator$getstatus(?)", inParamArr);
        return selections;
    }

    @Override
    public String getRussianMlfb(String mlfb) {
        Object[] inParamArr = {mlfb};
        List<String> list = callFunction(String.class, "configurator$sub$changetorus(?)", inParamArr);
        return list.get(0);
    }


    private <T> List<T> callFunction(Class<T> tClass, String procedureName, Object[] inParamArr) {
        List<T> list = new ArrayList<>();
        int j = 2;
        try {
            Connection conn = jdbcTemplate.getDataSource().getConnection();
            conn.setAutoCommit(false);
            CallableStatement proc = conn.prepareCall("{? = call " + procedureName + " }");
            if (inParamArr != null) {
                for (int i = 0; i < inParamArr.length; i++) {
                    if (inParamArr[i] instanceof String) {
                        proc.setString(j, (String) inParamArr[i]);
                        j++;
                    } else if (inParamArr[i] instanceof Integer) {
                        proc.setInt(j, (Integer) inParamArr[i]);
                        j++;
                    }
                }
            }
            proc.registerOutParameter(1, Types.OTHER);
            proc.execute();
            ResultSet results = (ResultSet) proc.getObject(1);
            while (results.next()) {
                if (tClass.isAssignableFrom(SensorTypeEntity.class)) {
                    SensorTypeEntity sensorTypeEntity = new SensorTypeEntity();
                    sensorTypeEntity.setId((int) results.getObject(1));
                    sensorTypeEntity.setName(results.getObject(2).toString());
                    sensorTypeEntity.setDescr(results.getObject(3).toString());
                    sensorTypeEntity.setMlfbstandart(results.getObject(4).toString());
                    list.add(tClass.cast(sensorTypeEntity));
                } else if (tClass.isAssignableFrom(SelectionEntity.class)) {
                    SelectionEntity selectionEntity = new SelectionEntity();
                    selectionEntity.setId((int) results.getObject(1));
                    selectionEntity.setGroup(results.getObject(2).toString());
                    if (results.getObject(3) != null) {
                        selectionEntity.setName(results.getObject(3).toString());
                    }
                    selectionEntity.setMain((boolean)results.getObject(4));
                    list.add(tClass.cast(selectionEntity));
                } else if (tClass.isAssignableFrom(SelectOptionEntity.class)) {
                    SelectOptionEntity selectOptionEntity = new SelectOptionEntity();
                    selectOptionEntity.setId((int) results.getObject(1));
                    selectOptionEntity.setName(results.getObject(2).toString());
                    selectOptionEntity.setGroup(results.getObject(3).toString());
                    if (results.getObject(4) != null) {
                        selectOptionEntity.setDescr(results.getObject(4).toString());
                    }
                    selectOptionEntity.setPossible((boolean) results.getObject(5));
                    list.add(tClass.cast(selectOptionEntity));
                } else if (tClass.isAssignableFrom(Sensors.class)) {
                    Sensors sensors = new Sensors();
                    sensors.setId(results.getInt(1));
                    sensors.setName(results.getString(2));
                    sensors.setRusname(results.getString(3));
                    sensors.setDescr(results.getString(4));
                    sensors.setMlfbstandart(results.getString(5));
                    sensors.setImage(results.getString(6));
                    sensors.setRusmlfbstandart(results.getString(7));
                    list.add(tClass.cast(sensors));
                } else if (tClass.isAssignableFrom(SensorsLabels.class)) {
                    SensorsLabels sensorsLabels = new SensorsLabels();
                    sensorsLabels.setId((int) results.getObject(1));
                    sensorsLabels.setPosition(results.getObject(2).toString());
                    sensorsLabels.setName(results.getObject(3).toString());
                    sensorsLabels.setTab((int) results.getObject(4));
                    sensorsLabels.setEltype((int) results.getObject(5));
                    sensorsLabels.setActive(false);
                    sensorsLabels.setSelectType(Constant.STATUS.WARN);
                    list.add(tClass.cast(sensorsLabels));
                } else if (tClass.isAssignableFrom(SensorsOptionNames.class)) {
                    SensorsOptionNames sensorsOptionNames = new SensorsOptionNames();
                    sensorsOptionNames.setId(results.getInt(1));
                    sensorsOptionNames.setOption(results.getString(2));
                    sensorsOptionNames.setName(results.getString(3));
                    sensorsOptionNames.setTab(results.getInt(4));
                    sensorsOptionNames.setGroup(results.getString(5));
                    sensorsOptionNames.setPossible(results.getBoolean(6));
                    list.add(tClass.cast(sensorsOptionNames));
                } else if (tClass.isAssignableFrom(SensorStatus.class)) {
                    SensorStatus sensorStatus = new SensorStatus();
                    sensorStatus.setStatus(results.getInt(1));
                    sensorStatus.setMessage(results.getString(2));
                    sensorStatus.setRule1(results.getString(3));
                    sensorStatus.setRule2(results.getString(4));
                    sensorStatus.setRule3(results.getString(5));
                    sensorStatus.setRule4(results.getString(6));
                    list.add(tClass.cast(sensorStatus));
                } else if (tClass.isAssignableFrom(String.class)) {
                    String result = results.getString(1);
                    list.add(tClass.cast(result));
                }
            }
            results.close();
            proc.close();
            conn.close();
        } catch (SQLException ex) {
            log.error("Ошибка при выполнении функции "+procedureName, ex);
        }
        return list;
    }

}
