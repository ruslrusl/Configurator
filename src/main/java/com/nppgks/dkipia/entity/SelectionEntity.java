package com.nppgks.dkipia.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@ToString
public class SelectionEntity {

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
    private boolean isDisplay;

    @Getter
    @Setter
    private boolean isMain;//основной тип

    @Getter
    @Setter
    private int selectType;//0 - не выбрано, 1 - выбрано правильно, 2 -выбрано не правильно

    @Getter
    @Setter
    private SelectOptionEntity currentSelect;//что выбрано

    @Getter
    @Setter
    private List<SelectOptionEntity> selectOption;
}
