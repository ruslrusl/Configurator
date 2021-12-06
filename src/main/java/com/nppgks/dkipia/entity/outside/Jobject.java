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
    private List<Integer> type;

    @Getter
    @Setter
    private String sendto;

    @Getter
    @Setter
    private String sendmsg;

    @Getter
    @Setter
    private int number;
}
