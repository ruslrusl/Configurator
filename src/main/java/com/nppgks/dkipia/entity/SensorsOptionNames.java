package com.nppgks.dkipia.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@NoArgsConstructor
public class SensorsOptionNames {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String option;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int tab;

    @Getter
    @Setter
    private boolean possible;

    @Getter
    @Setter
    private boolean selected;

    @Getter
    @Setter
    private String group;

    @Getter
    @Setter
    private OptionParam optionParam;

    @SneakyThrows
    public String toString() {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(this);
    }
}
