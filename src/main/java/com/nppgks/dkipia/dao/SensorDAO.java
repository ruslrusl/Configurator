package com.nppgks.dkipia.dao;

import com.nppgks.dkipia.entity.*;
import com.nppgks.dkipia.entity.start.SModel;
import com.nppgks.dkipia.entity.start.Signal;
import com.nppgks.dkipia.entity.start.Type;

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

    /**
     * Получение отдельно конфигурации, B-строки, С-строки
     *
     * @param mlfb конфигурация полностью
     * @return список(конфигурация, B - строка, С - строка)
     */
    List<String> getSeparateMlfb(String mlfb);

    /**
     * Получение списка комплектующих
     *
     * @return список
     */
    List<Complete> getComplete();

    /**
     * Получение описания по конфигурации
     *
     * @param mlfb конфигурация полностью
     * @return полное конфигурация
     */
    SensorFull getSensorFull(String mlfb);

    /**
     * Сохранение комплектующих
     *
     * @param list список комплектующих из списка параметров
     * @return результат
     */
    boolean saveComplete(List<List<?>> list);

    /**
     * Получение списка с ценами
     *
     * @param idSensor ид датчика
     * @return список
     */
    List<Price> getPrice(int idSensor);

    /**
     * Сохранение цен
     *
     * @param list список комплектующих из списка параметров
     * @return результат
     */
    boolean savePrice(List<List<?>> list);

    /**
     * Получение типа датчика
     *
     * @return список
     */
    List<Type> getType();

    /**
     * Получение моделей датчика по типу
     *
     * @param idType идентификатор типа
     * @return список
     */
    List<SModel> getModel(int idType);

    /**
     * Получение сигналов по типу и модели датчика
     *
     * @param idType  идентификатор типа
     * @param idModel идентификатор модели
     * @return список
     */
    List<Signal> getSignal(int idType, int idModel);
}
