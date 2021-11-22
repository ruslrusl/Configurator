package com.nppgks.dkipia.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nppgks.dkipia.util.Constant;
import lombok.*;

@NoArgsConstructor
public class OptionParam {

    @Getter
    @Setter
    String param1;
    @Getter
    @Setter
    String param2;
    @Getter
    @Setter
    String param3;

    @Getter
    @Setter
    String value;

    public OptionParam(String wholeWord) {
        String y = wholeWord.substring(0, 3);
        value = wholeWord.substring(4).trim();
        if (y.equalsIgnoreCase("Y01") || y.equalsIgnoreCase("Y02") || y.equalsIgnoreCase("Y22")|| y.equalsIgnoreCase("Y23")) {
            String[] str = value.split(Constant.MLFB.DELIMETER_POINTS);
            if (str.length == 2) {
                this.setParam1(str[0].trim());
                String[] str2 = str[1].trim().split("\\s+");
                this.setParam2(str2[0].trim());
                this.setParam3(str2[1].trim());
            }
        } else {
            param1 = value;
        }
    }

    @SneakyThrows
    public String toString() {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(this);
    }
}
