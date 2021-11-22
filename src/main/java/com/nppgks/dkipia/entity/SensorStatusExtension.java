package com.nppgks.dkipia.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SensorStatusExtension {

    @Getter
    @Setter
    private int status;

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private List<String> rule1;

    @Getter
    @Setter
    private List<String> rule2;

    @Getter
    @Setter
    private List<String> rule3;
}
