package com.nppgks.dkipia.entity;

import com.nppgks.dkipia.util.Constant;
import com.nppgks.dkipia.util.Util;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@ToString
public class SensorTypeEntity {

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String descr;

    @Getter
    @Setter
    private String mlfbstandart;

    @Getter
    @Setter
    private String mlfb;

    @Getter
    @Setter
    private List<String> mlfbB;

    @Getter
    @Setter
    private List<String> mlfbC;

    public void setOneMlfbB(String mlfbBstr) {
        if (mlfbB == null) {
            mlfbB = new ArrayList();
        }
        if (!mlfbB.contains(mlfbBstr)) {
            mlfbB.add(mlfbBstr);
        }
    }

    public void setOneMlfbC(String option, String mlfbCstr) {
        if (mlfbC == null) {
            mlfbC = new ArrayList();
        }
        Optional<String> optional = mlfbC.stream().filter(x -> x.contains(option + ":")).findFirst();
        if (optional.isPresent()) {
            mlfbC.replaceAll(s -> s.contains(option + ":") ? mlfbCstr : s);
        } else {
            mlfbC.add(mlfbCstr);
        }
    }

    public void removeOneMlfbB(String mlfbBstr) {
        if (mlfbB != null && mlfbBstr != null) {
            mlfbB.remove(mlfbBstr);
        }
    }

    public void removeOneMlfbC(String mlfbCstr) {
        if (mlfbC != null && mlfbCstr != null) {
            mlfbC.removeIf(x -> x.contains(mlfbCstr + ":"));
        }
    }

    public void setMlfbBString(String mlfbBstr) {
        if (mlfbBstr != null && !mlfbBstr.isEmpty()) {
            mlfbB = new ArrayList(Arrays.asList(mlfbBstr.split(Constant.MLFB.OPTION_DELIMETER)));
        }
    }

    public void setMlfbCString(String mlfbCstr) {
        if (mlfbCstr != null && !mlfbCstr.isEmpty()) {
            mlfbC = new ArrayList(Arrays.asList(Util.getMlfbCString(mlfbCstr)));
        }
    }

}
