package com.nppgks.dkipia.entity;

import lombok.*;

@NoArgsConstructor
@ToString
public class SelectOptionEntity {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String group;

    @Getter
    @Setter
    private String descr;

    @Getter
    @Setter
    private boolean isPossible;

    @Getter
    @Setter
    private boolean isSelected;

    @Getter
    @Setter
    private OptionParam optionParam;
}
