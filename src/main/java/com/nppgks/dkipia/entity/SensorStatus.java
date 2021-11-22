package com.nppgks.dkipia.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@NoArgsConstructor
public class SensorStatus {

    @Getter
    @Setter
    private int status;

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private String rule1;

    @Getter
    @Setter
    private String rule2;

    @Getter
    @Setter
    private String rule3;

    @Getter
    @Setter
    private String rule4;

    @SneakyThrows
    public String toString() {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(this);
    }
}
