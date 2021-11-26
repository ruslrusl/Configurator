package com.nppgks.dkipia.entity.outside;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

@NoArgsConstructor
public class Jcomplete {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String descr;

    @Getter
    @Setter
    private String price;

    @Getter
    @Setter
    private String count;

    @Getter
    @Setter
    private String pricecount;

    @Getter
    @Setter
    private String coef;

    @Getter
    @Setter
    private String pricecoef;

    @Getter
    @Setter
    private String pricetotal;

    @Getter
    @Setter
    private String unit;

    @Getter
    @Setter
    private String provider;

    @SneakyThrows
    public String toString() {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(this);
    }
}
