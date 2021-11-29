package com.nppgks.dkipia.service;

import java.util.List;

public interface DataService {
    /**
     * Вставка данных
     *
     * @param id
     * @param mlfb
     */
    public void insertData(String id, String mlfb);

    /**
     * Получение данных
     *
     * @param id
     * @return
     */
    public List<String> getDataList(String id);
}
