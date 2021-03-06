package com.nppgks.dkipia.service;

import com.nppgks.dkipia.entity.outside.Jobject;
import com.nppgks.dkipia.entity.outside.Jsensor;

import java.util.List;

public interface ExcelService {

    /**
     * Преобразование строки json формата в объект
     *
     * @param json строка
     * @return объект
     */
    Jobject convertFromJson(String json);

    /**
     * Формирование файла
     *
     * @param jsensorList список оборудований
     * @param type        тип формирования файла
     * @param number      номер корзины
     * @return название файла
     */
    String generateFile(List<Jsensor> jsensorList, int type, int number);

    /**
     * Формирование объекта из excel Файла
     *
     * @param fileDir excel Файл
     * @return объект
     */
    Jobject generateObjectFromFile(String fileDir);
}
