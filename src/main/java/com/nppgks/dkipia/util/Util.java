package com.nppgks.dkipia.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nppgks.dkipia.entity.SensorStatus;
import com.nppgks.dkipia.entity.SensorStatusExtension;
import com.nppgks.dkipia.entity.Sensors;
import com.nppgks.dkipia.entity.SensorsLabels;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Util {

    public final static boolean ISTEST;

    static {
        boolean ISTEST1;
        try {
            ISTEST1 = false;
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    if (i.getHostAddress().equalsIgnoreCase("192.168.61.138")) {
                        ISTEST1 = true;
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            ISTEST1 = false;
        }
        ISTEST = ISTEST1;
        System.out.println("Is test machine = " + ISTEST1);
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
                result = Arrays.stream(split)
                        .map(o -> o = o.substring(1))
                        .collect(Collectors.toList());
            } else if (delimeter == 4) {
                String[] split = str.split(Constant.MLFB.OPTION_DELIMETER);
                for (String s : split) {
                    String[] split2 = s.split(Constant.MLFB.DELIMETER_SPACE);
                    result.addAll(Arrays.asList(split2));
                }
            }
        }
        return result;
    }

    public static String concatenateString(List<String> list, int delimeter) {
        String str = "";
        if (list != null) {
            if (delimeter == 0) {//по букве
                str = String.join("", list);
            } else if (delimeter == 1) {
                str = String.join(Constant.MLFB.DELIMETER_PLUS, list);
            } else if (delimeter == 2) {
                str = String.join(Constant.MLFB.DELIMETER_SPACE, list);
            } else if (delimeter == 3) {
                str = list.stream().map(o -> o = Constant.MLFB.DELIMETER_OPEN_BRACKET + o + Constant.MLFB.DELIMETER_CLOSE_BRACKET)
                        .collect(Collectors.joining(Constant.MLFB.DELIMETER_EMPTY));
            }
        }
        return str;
    }

    public static String generateFullMlfb(Sensors sensor) {
        String result = sensor.getMlfb() + "Z ";
        if (sensor.getMlfbB() != null && !sensor.getMlfbB().isEmpty()) {
            result = result + sensor.getMlfbB();
            if (sensor.getMlfbC() != null && !sensor.getMlfbC().isEmpty()) {
                result = result + sensor.getMlfbC();
            }
        }
        return result;
    }

    public static SensorStatusExtension generateSensorStatusExt(List<SensorStatus> statusList, List<SensorsLabels> sensorsLabels) {
        String styleColorRed = "color:red;";
        String styleColorGreen = "color:green;";
        String styleColor = "";
        SensorStatusExtension sse = new SensorStatusExtension();
        if (statusList != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < statusList.size(); i++) {
                SensorStatus status = statusList.get(i);
                if (sse.getStatus() < status.getStatus()) {
                    sse.setStatus(status.getStatus());
                    if (status.getStatus() == Constant.STATUS.OK) {
                        styleColor = styleColorGreen;
                    } else if (status.getStatus() == Constant.STATUS.ERROR) {
                        styleColor = styleColorRed;
                    }
                }
                if (status.getMessage() != null) {
                    sb.append("<tr><td style=\"" + styleColor + "\">" + status.getMessage() + "</td></tr>");
                    if (status.getRule1() != null && !status.getRule1().isEmpty()) {
                        generateRuleString(sensorsLabels, sb, status.getRule1(), styleColor);
                    }
                    if (status.getRule2() != null && !status.getRule2().isEmpty()) {
                        generateRuleString(sensorsLabels, sb, status.getRule2(), styleColor);
                    }
                    if (status.getRule3() != null && !status.getRule3().isEmpty()) {
                        generateRuleString(sensorsLabels, sb, status.getRule3(), styleColor);
                    }
                    if (status.getRule4() != null && !status.getRule4().isEmpty()) {
                        generateRuleString(sensorsLabels, sb, status.getRule4(), styleColor);
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
        if (str != null && !str.isEmpty()) {
            String[] split = str.trim().split(Constant.MLFB.DELIMETER_EQUAL);
            if (isFirst && split.length > 0) {
                result = split[0].trim();
            } else if (!isFirst && split.length > 1) {
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
                    .filter(s -> s.getPosition().equals(first))
                    .findFirst();
            if (optional.isPresent()) {
                returnRule = optional.get().getName() + " - опция [" + second + "]";
            }
        }
        sb.append("<tr><td style=\"" + stylePaddingLeft + styleColor + "\" class=\"cursorpointer\"><span onclick=\"jumpNextGroup(0,'" + Util.getRuleValue(rule, true) + "', 0, this)\"> =>" + returnRule + "</span></td></tr>");
    }

    public static String getDir() {
        return Util.ISTEST ? Constant.FILE.TEST_DIRECTORY : Constant.FILE.DIRECTORY;
    }

    public static String generateFileNameWithDirectory(String name, int number) {
        DateTimeFormatter timeStampPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dir = Util.getDir();
//        String result = dir+number+"_"+name+"_"+timeStampPattern.format(java.time.LocalDateTime.now())+Constant.FILE.EXTENSION;
        String result = dir + number + "_" + name + Constant.FILE.EXTENSION;
        return result;
    }

    public static String getFileNameFromTemplate(int type) {
        String dir = Util.getDir();
        String result = "";
        if (type == Constant.FILE.EXPORT_TYPE_TKP) {
            result = dir + Constant.FILE.FILENAME_TKP;
        } else if (type == Constant.FILE.EXPORT_TYPE_SPECIFICATION) {
            result = dir + Constant.FILE.FILENAME_SPECIFICATION;
        }
        return result;
    }

    public static String convertStringFromJson(String json) {
        if (json != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                String str = mapper.readValue(json, String.class);
                str = str.trim();
                return str;
            } catch (JsonProcessingException e) {
                log.error("Ошибка при парсинге json", e);
                return null;
            }
        } else {
            return null;
        }
    }
}
