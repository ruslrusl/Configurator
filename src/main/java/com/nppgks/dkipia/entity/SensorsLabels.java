package com.nppgks.dkipia.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class SensorsLabels {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String position;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int tab;

    @Getter
    @Setter
    private int eltype;

    @Getter
    @Setter
    private boolean isActive;

    @Getter
    @Setter
    private int selectType;

    @Getter
    @Setter
    private SensorsOptionNames currentOptionSelect;

    @Getter
    @Setter
    private List<SensorsOptionNames> sensorsOptionNames;

    public void setOneSensorOptionNames(SensorsOptionNames sensorsOptionNam) {
        if (sensorsOptionNames==null) {
            sensorsOptionNames = new ArrayList<>();
        }
        sensorsOptionNames.add(sensorsOptionNam);
    }

    @SneakyThrows
    public String toString() {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(this);
    }
}
