package com.nppgks.dkipia.entity.outside;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Jobject {
    @Getter
    @Setter
    private List<Jsensor> sensors;

    @Getter
    @Setter
    private int type;
}
