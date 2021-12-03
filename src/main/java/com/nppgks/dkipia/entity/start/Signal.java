package com.nppgks.dkipia.entity.start;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

public class Signal {
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int ordernumb;

    @Getter
    @Setter
    private int idSensor;

    @SneakyThrows
    public String toString() {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(this);
    }
}
