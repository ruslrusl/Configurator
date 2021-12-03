package com.nppgks.dkipia.dao;

import com.nppgks.dkipia.entity.*;
import com.nppgks.dkipia.entity.start.SModel;
import com.nppgks.dkipia.entity.start.Signal;
import com.nppgks.dkipia.entity.start.Type;
import com.nppgks.dkipia.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.DecimalFormat;
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

    @Override
    public List<String> getSeparateMlfb(String mlfb) {
        Object[] inParamArr = {mlfb};
        return callFunction(String.class, "configurator$separatemlfb(?)", inParamArr);
    }

    @Override
    public List<Complete> getComplete() {
        return callFunction(Complete.class, "configurator$getcomplete()", null);
    }

    @Override
    public List<Price> getPrice(int idSensor) {
        Object[] inParamArr = {idSensor};
        return callFunction(Price.class, "configurator$getprice(?)", inParamArr);
    }

    @Override
    public SensorFull getSensorFull(String mlfb) {
        Object[] inParamArr = {mlfb};
        List<SensorFull> list = callFunction(SensorFull.class, "configurator$getsensordescbymlfb(?)", inParamArr);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public boolean saveComplete(List<List<?>> list) {
        Object[] inParamArr = new Object[list.size()];
        for (int i = 0; i < list.size(); i++) {
            inParamArr[i] = list.get(i);
        }
        return callFunctionForUpdate(Boolean.class, "configurator$saveorupdatecomplete(?,?,?,?,?,?,?,?)", inParamArr);
    }

    @Override
    public boolean savePrice(List<List<?>> list) {
        Object[] inParamArr = new Object[list.size()];
        for (int i = 0; i < list.size(); i++) {
            inParamArr[i] = list.get(i);
        }
        return callFunctionForUpdate(Boolean.class, "configurator$saveprice(?,?)", inParamArr);
    }

    @Override
    public List<Type> getType() {
        return callFunction(Type.class, "configurator$getstarttype()", null);
    }

    @Override
    public List<SModel> getModel(int idType) {
        Object[] inParamArr = {idType};
        return callFunction(SModel.class, "configurator$getstartmodel(?)", inParamArr);
    }

    @Override
    public List<Signal> getSignal(int idType, int idModel) {
        Object[] inParamArr = {idType, idModel};
        return callFunction(Signal.class, "configurator$getstartsignalwithmlfb(?, ?)", inParamArr);
    }

    private <T> boolean callFunctionForUpdate(Class<T> tClass, String procedureName, Object[] inParamArr) {
        int j = 2;
        boolean result = false;
        try (Connection conn = jdbcTemplate.getDataSource().getConnection()) {
            conn.setAutoCommit(true);
            CallableStatement proc = conn.prepareCall("{? = call " + procedureName + " }");
            if (inParamArr != null) {
                for (int i = 0; i < inParamArr.length; i++) {
                    if (inParamArr[i] instanceof String) {
                        proc.setString(j, (String) inParamArr[i]);
                        j++;
                    } else if (inParamArr[i] instanceof Integer) {
                        proc.setInt(j, (Integer) inParamArr[i]);
                        j++;
                    } else if (inParamArr[i] instanceof List) {
                        List inParamlist = (List) inParamArr[i];
                        String arrType = "text";
                        if (inParamlist.get(0) instanceof Integer) {
                            arrType = "integer";
                        } else if (inParamlist.get(0) instanceof Double) {
                            arrType = "numeric";
                        }
                        final java.sql.Array sqlArray = conn.createArrayOf(arrType, ((List) inParamArr[i]).toArray());
                        proc.setArray(j, sqlArray);
                        j++;
                    }
                }
            }
            proc.registerOutParameter(1, Types.BOOLEAN);
            proc.execute();
            result = (Boolean) proc.getObject(1);
            proc.close();
        } catch (SQLException ex) {
            log.error("Ошибка при выполнении функции " + procedureName, ex);
        }
        return result;
    }


    private <T> List<T> callFunction(Class<T> tClass, String procedureName, Object[] inParamArr) {
        List<T> list = new ArrayList<>();
        int j = 2;
        try (Connection conn = jdbcTemplate.getDataSource().getConnection()) {
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
                    } else if (inParamArr[i] instanceof List) {
                        List inParamlist = (List) inParamArr[i];
                        String arrType = "text";
                        if (inParamlist.get(0) instanceof Integer) {
                            arrType = "integer";
                        }
                        final java.sql.Array sqlArray = conn.createArrayOf(arrType, ((List) inParamArr[i]).toArray());
                        proc.setArray(j, sqlArray);
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
                } else if (tClass.isAssignableFrom(Boolean.class)) {
                    Boolean result = results.getBoolean(1);
                    list.add(tClass.cast(result));
                } else if (tClass.isAssignableFrom(Complete.class)) {
                    Complete complete = new Complete();
                    complete.setId(results.getInt(1));
                    complete.setName(results.getString(2));
                    complete.setPrice(getFormattedDouble(results.getDouble(3)));
                    complete.setCoef(results.getDouble(4));
                    complete.setOrdernumb(results.getInt(5));
                    complete.setIsused(results.getInt(6));
                    complete.setDescr(results.getString(7));
                    complete.setProvider(results.getString(8));
                    complete.setUnit(results.getString(9));
                    list.add(tClass.cast(complete));
                } else if (tClass.isAssignableFrom(SensorFull.class)) {
                    SensorFull sensorFull = new SensorFull();
                    sensorFull.setId(results.getInt(1));
                    sensorFull.setMlfb(results.getString(2));
                    sensorFull.setRusmlfb(results.getString(3));
                    sensorFull.setDescr(results.getString(4));
                    sensorFull.setPrice(getFormattedDouble(results.getDouble(5)));
                    list.add(tClass.cast(sensorFull));
                } else if (tClass.isAssignableFrom(Price.class)) {
                    Price price = new Price();
                    price.setId(results.getInt(1));
                    price.setName(results.getString(2));
                    price.setPosition(results.getString(3));
                    price.setOption(results.getString(4));
                    price.setPrice(results.getDouble(5));
                    list.add(tClass.cast(price));
                } else if (tClass.isAssignableFrom(Type.class)) {
                    Type obj = new Type();
                    obj.setId(results.getInt(1));
                    obj.setName(results.getString(2));
                    obj.setOrdernumb(results.getInt(3));
                    list.add(tClass.cast(obj));
                } else if (tClass.isAssignableFrom(SModel.class)) {
                    SModel obj = new SModel();
                    obj.setId(results.getInt(1));
                    obj.setName(results.getString(2));
                    obj.setOrdernumb(results.getInt(3));
                    list.add(tClass.cast(obj));
                } else if (tClass.isAssignableFrom(Signal.class)) {
                    Signal obj = new Signal();
                    obj.setId(results.getInt(1));
                    obj.setName(results.getString(2));
                    obj.setOrdernumb(results.getInt(3));
                    obj.setIdSensor(results.getInt(4));
                    list.add(tClass.cast(obj));
                }
            }
            results.close();
            proc.close();
        } catch (SQLException ex) {
            log.error("Ошибка при выполнении функции " + procedureName, ex);
        }
        return list;
    }

    private static String getFormattedDouble(Double d) {
        final DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d).replace(",", ".");
    }

}
