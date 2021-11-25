package com.nppgks.dkipia.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

public class Complete {
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String price;

    @Getter
    @Setter
    private Double coef;

    @Getter
    @Setter
    private int ordernumb;

    @Getter
    @Setter
    private int isused;

    @SneakyThrows
    public String toString() {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(this);
    }
}
