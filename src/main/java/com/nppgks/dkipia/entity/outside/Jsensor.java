package com.nppgks.dkipia.entity.outside;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class Jsensor {
    @Getter
    @Setter
    private String number;

    @Getter
    @Setter
    private String mlfbrus;

    @Getter
    @Setter
    private String mlfb;

    @Getter
    private String descr;

    public void setDescr(String descr) {
        if (descr != null)
            descr = descr.replaceAll("<br>", "\n");
        this.descr = descr;
    }

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
    private String totalpricecoef;

    @Getter
    @Setter
    private String totalpricetotal;

    @Getter
    @Setter
    private List<Jcomplete> complete;

    public void setOneComplete(Jcomplete jcomplete) {
        if (complete==null) {
            complete = new ArrayList<>();
        }
        complete.add(jcomplete);
    }

    @SneakyThrows
    public String toString() {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(this);
    }

}
