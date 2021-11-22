package com.nppgks.dkipia.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;

@NoArgsConstructor
@ToString
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

//    public String getValueWithoutCaption() {
//        if (value != null && !value.isEmpty()) {
//            return value.substring(4).trim();
//        } else {
//            return "";
//        }
//    }

    public OptionParam(String wholeWord) {
        String y = wholeWord.substring(0, 3);
        value = wholeWord.substring(4).trim();
        if (y.equalsIgnoreCase("Y01") || y.equalsIgnoreCase("Y02") || y.equalsIgnoreCase("Y22")|| y.equalsIgnoreCase("Y23")) {
            String str[] = value.split("\\.\\.\\.");
            if (str != null && str.length == 2) {
                this.setParam1(str[0].trim());
                String[] str2 = str[1].trim().split("\\s+");
                this.setParam2(str2[0].trim());
                this.setParam3(str2[1].trim());
            }
        } else {
            param1 = value;
        }
    }
}
