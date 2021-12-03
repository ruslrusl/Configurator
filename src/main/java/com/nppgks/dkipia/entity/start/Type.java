package com.nppgks.dkipia.entity.start;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

public class Type {
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int ordernumb;

    @SneakyThrows
    public String toString() {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(this);
    }
}
