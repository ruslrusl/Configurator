package com.nppgks.dkipia.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class Sensors {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String rusname;

    @Getter
    @Setter
    private String descr;

    @Getter
    @Setter
    private String mlfbstandart;

    @Getter
    @Setter
    private String image;

    @Getter
    @Setter
    private List<SensorsLabels> sensorsLabels;

    @Getter
    @Setter
    private String mlfb;

    @Getter
    @Setter
    private String mlfbB;

    @Getter
    @Setter
    private String mlfbC;

    @Getter
    @Setter
    private String rusmlfbstandart;

    public void setSensorLabel(SensorsLabels sensorLabel) {
        if (sensorsLabels==null) {
            sensorsLabels = new ArrayList<>();
        }
        sensorsLabels.add(sensorLabel);
    }

    @SneakyThrows
    public String toString() {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(this);
    }

}
