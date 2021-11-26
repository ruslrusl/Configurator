import com.nppgks.dkipia.entity.Sensors;
import com.nppgks.dkipia.entity.SensorsLabels;
import com.nppgks.dkipia.service.SensorService;
import com.nppgks.dkipia.service.SensorServiceImpl;
import com.nppgks.dkipia.util.Constant;
import com.nppgks.dkipia.util.Util;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MyTestClass {



    public static void main(String[] args) {

//        System.out.println(Util.getMlfbB("asf", "ZA1", "A02"));

//        String str = "{Y123:124qw}{Y7564:3rfs}";
//        String []sl = str.split("}");
//        System.out.println(sl.length);
//        System.out.println(Arrays.toString(sl));
//        List<String> listMlfbс =  Util.separateString("{Y99:sdfsdf}", 3);


//        String[] split = "{Y99:sdfsdf}".split(Constant.MLFB.DELIMETER_CLOSE_BRACKET);
//        System.out.println(split.length);
//        List<String> result = Arrays.asList(split)
//                .stream()
//                .map(o-> o = o.substring(1))
//                .collect(Collectors.toList());





//        List<String> list = new ArrayList<>();
//        list.add("qwsd");
//        list.add("asfas");
//        String str = list.stream().map(o-> o = Constant.MLFB.DELIMETER_OPEN_BRACKET+o+Constant.MLFB.DELIMETER_CLOSE_BRACKET)
//                            .collect(Collectors.joining(Constant.MLFB.DELIMETER_EMPTY));
//        System.out.println(result);

    }

    public static List<String> mlfbC;

    public static void setOneMlfbC(String mlfbCstr) {
        if (mlfbC == null) {
            mlfbC = new ArrayList();
        }
        Optional<String> optional = mlfbC.stream().filter(x -> x.contains(mlfbCstr + ":")).findFirst();
        if (optional.isPresent()) {
            mlfbC.replaceAll(s -> s.contains(mlfbCstr + ":") ? mlfbCstr : s);
        } else {
            mlfbC.add(mlfbCstr);
        }
    }
}
