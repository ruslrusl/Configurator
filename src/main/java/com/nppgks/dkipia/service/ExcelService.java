package com.nppgks.dkipia.service;

import com.nppgks.dkipia.entity.outside.Jobject;

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
     * @param jobject источник
     * @return название файла
     */
    String generateFile(Jobject jobject);
}
