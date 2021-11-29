package com.nppgks.dkipia.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataServiceImpl implements DataService {


    private static Map<String, List<String>> dataStoreMap;

    @Override
    public void insertData(String id, String mlfb) {
        List<String> listData = getDataList(id);
        listData.add(mlfb);
        dataStoreMap.put(id, listData);
    }

    @Override
    public List<String> getDataList(String id) {
        if (dataStoreMap == null) {
            dataStoreMap = new HashMap<>();
        }
        List<String> dataList = dataStoreMap.get(id);
        if (dataList != null) {
            return dataList;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void removeData(String id, String mlfb) {
        List<String> listData = getDataList(id);
        if (listData != null) {
            listData.remove(mlfb);
        }
    }
}
