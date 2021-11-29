package com.nppgks.dkipia.service;

import com.nppgks.dkipia.entity.*;

import java.util.List;

public interface SensorService {
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
     * Установка активным данную позицию и получение опции позиций
     *
     * @param sensorsLabels список позиций
     * @param position      номер позиции
     */
    void setActiveLabelByPosition(List<SensorsLabels> sensorsLabels, String position);

    /**
     * Установка активным данную позицию и получение опции позиций по правилам
     *
     * @param sensorsLabels список позиций
     * @param position      номер позиции
     * @param mlfb          конфигурация
     */
    void setActiveLabelByPosition(List<SensorsLabels> sensorsLabels, String position, String mlfb);

    /**
     * Установка выбранной опции для позиции
     *
     * @param sensorsLabels список позиций
     * @param mlfb          конфигурация
     */
    void setSelectedPositionsByMlfb(List<SensorsLabels> sensorsLabels, String mlfb);

    /**
     * Установка конфигурации и опций
     *
     * @param sensor датчик
     * @param mlfb   конфигурация
     * @param mlfbB  B-строка
     * @param mlfbC  С-строка
     */
    void setMlfbWithOptions(Sensors sensor, String mlfb, String mlfbB, String mlfbC);

    /**
     * Установка выбранной опции B-строки для позиции
     *
     * @param sensorsLabels список позиций
     * @param idSensor      ид датчика
     * @param mlfbB         B-строка
     */
    void setSelectedPositionsByMlfbB(List<SensorsLabels> sensorsLabels, int idSensor, String mlfbB);

    /**
     * Установка Y опций
     *
     * @param sensorsLabels список позиций
     * @param mlfbC         С-строка
     */
    void setYoptions(List<SensorsLabels> sensorsLabels, String mlfbC);

    /**
     * Получение B-строки и С-строки
     *
     * @param sensorsLabels список позиций
     * @param mlfbB         B-строка
     * @param mlfbC         С-строка
     * @param group         группа/позиция
     * @param option        опция
     * @param mlfbCText     текстовая С-строка
     * @return масив [B-строка, С-строка]
     */
    String[] getMlfbBAndMlfbC(List<SensorsLabels> sensorsLabels, String mlfbB, String mlfbC, String group, String option, String mlfbCText);

    /**
     * Получение списка статуса
     *
     * @param mlfb конфигурация
     * @return список
     */
    List<SensorStatus> getSensorStatus(String mlfb);

    /**
     * Установка статусов по позициям
     *
     * @param sensorsLabels список позиций
     * @param sensorStatus  список статуса
     */
    void setSelectTypeByStatus(List<SensorsLabels> sensorsLabels, List<SensorStatus> sensorStatus);

    /**
     * Получение русской конфигурации
     *
     * @param mlfb конфигурация
     * @return строка
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
     * @param isused 1-только используемые
     * @return список
     */
    List<Complete> getComplete(boolean isused);

    /**
     * Получение описания по конфигурации
     *
     * @param mlfb конфигурация полностью
     * @return
     */
    SensorFull getSensorFull(String mlfb);

    /**
     * Получение спика описания по конфигурации
     *
     * @param mlfb конфигурация полностью
     * @return список
     */
    List<SensorFull> getSensorFullList(String mlfb);

    List<SensorFull> getSensorFullList(List<String> mlfbList);

}
