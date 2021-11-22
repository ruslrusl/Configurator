package com.nppgks.dkipia.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
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
}
