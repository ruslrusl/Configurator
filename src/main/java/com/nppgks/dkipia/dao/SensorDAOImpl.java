package com.nppgks.dkipia.dao;

import com.nppgks.dkipia.entity.SensorStatus;
import com.nppgks.dkipia.entity.Sensors;
import com.nppgks.dkipia.entity.SensorsLabels;
import com.nppgks.dkipia.entity.SensorsOptionNames;
import com.nppgks.dkipia.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class SensorDAOImpl implements SensorDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Sensors> getSensors() {
        return callFunction(Sensors.class, "configurator$selectsensors()", null);
    }

    @Override
    public Sensors getSensorById(int idSensor) {
        Object[] inParamArr = {idSensor};
        List<Sensors> list = callFunction(Sensors.class, "configurator$selectsensorsbyid(?)", inParamArr);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<SensorsLabels> getSensorLabels(int idSensor) {
        Object[] inParamArr = {idSensor};
        return callFunction(SensorsLabels.class, "configurator$selectlabels(?)", inParamArr);
    }

    @Override
    public List<SensorsOptionNames> getSensorOptions(int idSensorLabels) {
        Object[] inParamArr = {idSensorLabels};
        return callFunction(SensorsOptionNames.class, "configurator$selectoptions(?)", inParamArr);
    }

    @Override
    public List<SensorsOptionNames> getSensorOptionsByRule(String mlfb, int idSensorLabels) {
        Object[] inParamArr = {mlfb, idSensorLabels};
        return callFunction(SensorsOptionNames.class, "configurator$selectoptionsbyrule(?, ?)", inParamArr);
    }

    @Override
    public List<SensorsOptionNames> getSensorOptionsByMlfb(String mlfb) {
        Object[] inParamArr = {mlfb};
        return callFunction(SensorsOptionNames.class, "configurator$getselectedoptionsbymlfb(?)", inParamArr);
    }

    @Override
    public List<SensorsOptionNames> getSensorOptionsByMlfbB(int idSensor, String mlfbB) {
        Object[] inParamArr = {idSensor, mlfbB};
        return callFunction(SensorsOptionNames.class, "configurator$getselectedoptionsbyb(?, ?)", inParamArr);
    }

    @Override
    public List<SensorStatus> getSensorStatus(String mlfb) {
        Object[] inParamArr = {mlfb};
        return callFunction(SensorStatus.class, "configurator$getstatus(?)", inParamArr);
    }

    @Override
    public String getRussianMlfb(String mlfb) {
        Object[] inParamArr = {mlfb};
        List<String> list = callFunction(String.class, "configurator$sub$changetorus(?)", inParamArr);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
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
                if (tClass.isAssignableFrom(Sensors.class)) {
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
            log.error("Ошибка при выполнении функции " + procedureName, ex);
        }
        return list;
    }

}
