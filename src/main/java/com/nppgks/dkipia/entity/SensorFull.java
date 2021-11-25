package com.nppgks.dkipia.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

public class SensorFull {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String mlfb;

    @Getter
    @Setter
    private String rusmlfb;

    @Getter
    @Setter
    private String descr;

    @Getter
    @Setter
    private String price;

    @SneakyThrows
    public String toString() {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(this);
    }

}
