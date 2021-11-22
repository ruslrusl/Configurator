package com.nppgks.dkipia.util;

import com.nppgks.dkipia.entity.SensorStatus;
import com.nppgks.dkipia.entity.SensorStatusExtension;
import com.nppgks.dkipia.entity.Sensors;
import com.nppgks.dkipia.entity.SensorsLabels;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Util {

    public static String[] getMlfbCString(String mlfbCstr) {
        if (mlfbCstr != null && !mlfbCstr.isEmpty()) {
            List<String> options = new ArrayList<>();
            Pattern p = Pattern.compile("\\{(.*?)\\}");
            Matcher m = p.matcher(mlfbCstr);
            while (m.find()) {
                options.add(m.group(1));
            }
            return options.stream().toArray(String[]::new);
        }
        return null;
    }

    public static List<String> separateString(String str, int delimeter) {
        List<String> result = new ArrayList<>();
        if (str != null && !str.isEmpty()) {
            if (delimeter == 0) {//по букве
                for (int i = 0; i < str.length(); i++) {
                    result.add(String.valueOf(str.charAt(i)));
                }
            } else if (delimeter == 1) {
                String[] split = str.split(Constant.MLFB.OPTION_DELIMETER);
                result = new ArrayList(Arrays.asList(split));
            } else if (delimeter == 2) {
                String[] split = str.split(Constant.MLFB.DELIMETER_SPACE);
                result = new ArrayList(Arrays.asList(split));
            } else if (delimeter == 3) {
                String[] split = str.split(Constant.MLFB.DELIMETER_CLOSE_BRACKET);
                result = Arrays.asList(split)
                        .stream()
                        .map(o-> o = o.substring(1))
                        .collect(Collectors.toList());
            }
        }
        return result;
    }

    public static String concatenateString(List<String> list, int delimeter) {
        String str = "";
        if (list != null) {
            if (delimeter == 0) {//по букве
                str = list.stream().collect(Collectors.joining(""));
            } else if (delimeter == 1) {
                str = list.stream().collect(Collectors.joining(Constant.MLFB.DELIMETER_PLUS));
            } else if (delimeter == 2) {
                str = list.stream().collect(Collectors.joining(Constant.MLFB.DELIMETER_SPACE));
            } else if (delimeter == 3) {
                str = list.stream().map(o-> o = Constant.MLFB.DELIMETER_OPEN_BRACKET+o+Constant.MLFB.DELIMETER_CLOSE_BRACKET)
                        .collect(Collectors.joining(Constant.MLFB.DELIMETER_EMPTY));
            }
        }
        return str;
    }

    public static String getMlfbB(String mlfbb, String group, String option) {
        if (option != null && !option.equals("0") && !NumberUtils.isParsable(group)) {
            String result = "";
            if (mlfbb != null && !mlfbb.isEmpty() && !mlfbb.contains(option)) {
                result = mlfbb + Constant.MLFB.DELIMETER_PLUS;
                result = result + option;
            } else {
                result = option;
            }
            return result;
        }
        return mlfbb;
    }

    public static String generateFullMlfb(Sensors sensor) {
        String result =  sensor.getMlfb()+"Z ";
        if (sensor.getMlfbB()!=null && !sensor.getMlfbB().isEmpty()) {
            result = result + sensor.getMlfbB();
            if  (sensor.getMlfbC()!=null && !sensor.getMlfbC().isEmpty()) {
                result = result + sensor.getMlfbC();
            }
        }
        return result;
    }

    public static SensorStatusExtension generateSensorStatusExt(List<SensorStatus> statusList, List<SensorsLabels> sensorsLabels) {
        String stylePaddingLeft = "padding-left: 20px;";
        String styleColorRed = "color:red;";
        String styleColorGreen = "color:green;";
        String styleColor="";
        SensorStatusExtension sse = new SensorStatusExtension();
        if (statusList!=null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < statusList.size(); i++) {
                SensorStatus status = statusList.get(i);
                if (sse.getStatus()<status.getStatus()) {
                    sse.setStatus(status.getStatus());
                    if (status.getStatus()==Constant.STATUS.OK) {
                        styleColor = styleColorGreen;
                    } else if (status.getStatus()==Constant.STATUS.ERROR) {
                        styleColor = styleColorRed;
                    }
                }
                if (status.getMessage()!=null) {
                    sb.append("<tr><td style=\""+styleColor+"\">"+status.getMessage()+"</td></tr>");
                    if (status.getRule1()!=null && !status.getRule1().isEmpty()) {
                        generateRuleString(sensorsLabels, sb, status.getRule1(), styleColor);
                        //sb.append("<tr><td style=\""+stylePaddingLeft+styleColor+"\" class=\"cursorpointer\"><span onclick=\"jumpNextGroup(0,'"+Util.getRuleValue(status.getRule1(), true)+"', 0, this)\"> =>"+Util.generateRuleString(sensorsLabels,status.getRule1())+"</span></td></tr>");
                    }
                    if (status.getRule2()!=null && !status.getRule2().isEmpty()) {
                        generateRuleString(sensorsLabels, sb, status.getRule2(), styleColor);
                        //sb.append("<tr><td style=\""+stylePaddingLeft+styleColor+"\" class=\"cursorpointer\"> =>"+Util.generateRuleString(sensorsLabels,status.getRule2())+"</td></tr>");
                    }
                    if (status.getRule3()!=null && !status.getRule3().isEmpty()) {
                        generateRuleString(sensorsLabels, sb, status.getRule3(), styleColor);
                        //sb.append("<tr><td style=\""+stylePaddingLeft+styleColor+"\" class=\"cursorpointer\"> =>"+Util.generateRuleString(sensorsLabels,status.getRule3())+"</td></tr>");
                    }
                    if (status.getRule4()!=null && !status.getRule4().isEmpty()) {
                        generateRuleString(sensorsLabels, sb, status.getRule4(), styleColor);
                        //sb.append("<tr><td style=\""+stylePaddingLeft+styleColor+"\" class=\"cursorpointer\"> =>"+Util.generateRuleString(sensorsLabels,status.getRule3())+"</td></tr>");
                    }
                }
            }
            sse.setMessage(sb.toString());
        } else {
            sse.setStatus(Constant.STATUS.WARN);
            sse.setMessage(Constant.STATUS.NOT_COMPLETE);
        }
        return sse;
    }

    public static String getRuleValue(String str, boolean isFirst) {
        String result = "";
        if (str!=null && !str.isEmpty()) {
            String[] split = str.trim().split(Constant.MLFB.DELIMETER_EQUAL);
            if (isFirst && split.length>0) {
                result = split[0].trim();
            } else if (!isFirst && split.length>1) {
                result = split[1].trim();
            }
        }
        return result;
    }

    public static void generateRuleString(List<SensorsLabels> sensorsLabels, StringBuilder sb, String rule, String styleColor) {
        String stylePaddingLeft = "padding-left: 20px;";
        String returnRule = rule;
        String first = Util.getRuleValue(rule, true);
        String second = Util.getRuleValue(rule, false);
        if (!first.isEmpty()) {
            Optional<SensorsLabels> optional = sensorsLabels.stream()
                    .filter(s->s.getPosition().equals(first))
                    .findFirst();
            if (optional.isPresent()) {
                returnRule = optional.get().getName() + " - опция ["+second+"]";
            }
        }

        sb.append("<tr><td style=\""+stylePaddingLeft+styleColor+"\" class=\"cursorpointer\"><span onclick=\"jumpNextGroup(0,'"+Util.getRuleValue(rule, true)+"', 0, this)\"> =>"+returnRule+"</span></td></tr>");

        //return returnRule;
    }
}
