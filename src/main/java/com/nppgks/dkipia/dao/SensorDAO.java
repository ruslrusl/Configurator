package com.nppgks.dkipia.dao;

import com.nppgks.dkipia.entity.SensorStatus;
import com.nppgks.dkipia.entity.Sensors;
import com.nppgks.dkipia.entity.SensorsLabels;
import com.nppgks.dkipia.entity.SensorsOptionNames;

import java.util.List;

public interface SensorDAO {
    /**
     * Получение датчиков
     *
     * @return список
     */
    List<Sensors> getSensors();

    /**
     * Получение датчика по id
     *
     * @param idSensor ид датчика
     * @return датчик
     */
    Sensors getSensorById(int idSensor);

    /**
     * Получение названий позиции
     *
     * @param idSensor ид датчика
     * @return список
     */
    List<SensorsLabels> getSensorLabels(int idSensor);

    /**
     * Получение опции
     *
     * @param idSensorLabels ид позиции в БД
     * @return список
     */
    List<SensorsOptionNames> getSensorOptions(int idSensorLabels);

    /**
     * Получение опции по правилам для позиции
     *
     * @param mlfb           конфигурация
     * @param idSensorLabels id позиции
     * @return список
     */
    List<SensorsOptionNames> getSensorOptionsByRule(String mlfb, int idSensorLabels);

    /**
     * Получение опции для конфигурации
     *
     * @param mlfb конфигурация
     * @return список
     */
    List<SensorsOptionNames> getSensorOptionsByMlfb(String mlfb);

    /**
     * Получение опции для конфигурации
     *
     * @param idSensor ид датчика
     * @param mlfbB    конфигурация
     * @return список
     */
    List<SensorsOptionNames> getSensorOptionsByMlfbB(int idSensor, String mlfbB);

    /**
     * Получение статуса для конфигурации
     *
     * @param mlfb конфигурация
     * @return список
     */
    List<SensorStatus> getSensorStatus(String mlfb);

    /**
     * Получение преобраованного на русскую конфигурацию
     *
     * @param mlfb конфигурация
     * @return строку
     */
    String getRussianMlfb(String mlfb);
}
