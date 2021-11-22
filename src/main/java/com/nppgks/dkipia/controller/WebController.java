package com.nppgks.dkipia.controller;

import com.nppgks.dkipia.entity.*;
import com.nppgks.dkipia.service.SensorService;
import com.nppgks.dkipia.util.Constant;
import com.nppgks.dkipia.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WebController {

    @Autowired
    private SensorService sensorService;

    @GetMapping("/")
    public String goToConfigurator() {
        return "redirect:configurator";
    }

    @RequestMapping("/configurator")
    public String getSensorType(Model model) {
        List<Sensors>  sensors = sensorService.getSensors();
        model.addAttribute("sensors", sensors);
        return "configurator";
    }

    @GetMapping("/configurator/{id}")
    public String getMlfbSelectionGet(@PathVariable int id, Model model) {
        System.out.println("GetMapping");
        Sensors sensor = sensorService.getSensorById(id);
        System.out.println(sensor);
        List<SensorsLabels> sensorsLabels = sensorService.getSensorLabels(id);
        if (id<=21) {
            sensorService.setActiveLabelByPosition(sensorsLabels, "8");
        } else {
            sensorService.setActiveLabelByPosition(sensorsLabels, "6");
        }
        SensorStatusExtension sse =  Util.generateSensorStatusExt(null, null);
        String russianMlfb = sensorService.getRussianMlfb(sensor.getMlfbstandart());

        System.out.println(sensorsLabels);
        System.out.println(russianMlfb);

        model.addAttribute("sensor", sensor);
        model.addAttribute("sensorsLabels", sensorsLabels);
        model.addAttribute("mlfb", Util.separateString(sensor.getMlfbstandart(), 0));
        model.addAttribute("mlfbB", null);
        model.addAttribute("mlfbC", null);
        model.addAttribute("mlfbRus", Util.separateString(russianMlfb, 0));

//        model.addAttribute("Y21units", Constant.MLFB.UNITS);
        model.addAttribute("sensorStatus", sse);
        return "configuratorlabels";
    }

    @PostMapping("/configurator/{id}")
    public String getMlfbSelectionPost(@PathVariable int id, @RequestParam String mlfb, @RequestParam String mlfbB, @RequestParam String mlfbC, @RequestParam String group, @RequestParam String option, @RequestParam String mlfbCText, Model model) {
        System.out.println("PostMapping");
        System.out.println("mlfb = "+mlfb);
        System.out.println("mlfbB = "+mlfbB);
        System.out.println("mlfbC = "+mlfbC);
        System.out.println("mlfbCText = "+mlfbCText);
        System.out.println("option = "+option);
        System.out.println("group = "+group);

        Sensors sensor = sensorService.getSensorById(id);
        //формируем необходимые label
        List<SensorsLabels> sensorsLabels = sensorService.getSensorLabels(id);
        //выбираем только необходимую опции у label, а не все параметры
        sensorService.setActiveLabelByPosition(sensorsLabels, group);
        //формируем текущую строку B и C
        String[] mlfbBC = sensorService.getMlfbBAndMlfbC(sensorsLabels, mlfbB, mlfbC, group, option, mlfbCText);
        //сохраняем промежуточные данные
        sensorService.setMlfbWithOptions(sensor, mlfb, mlfbBC[0], mlfbBC[1]);

        String fullMlfb = Util.generateFullMlfb(sensor);
        sensorService.setActiveLabelByPosition(sensorsLabels, group, fullMlfb);

        //устанавливаем текущие выбранные позиции для основного типа
        sensorService.setSelectedPositionsByMlfb(sensorsLabels, sensor.getMlfb());
        //устанавливаем текущие выбранные позиции для опции
        sensorService.setSelectedPositionsByMlfbB(sensorsLabels, sensor.getId(), sensor.getMlfbB());

        sensorService.setYoptions(sensorsLabels, sensor.getMlfbC());

        System.out.println(fullMlfb);
        List<SensorStatus> sensorStatus = sensorService.getSensorStatus(fullMlfb);
        SensorStatusExtension sse =  Util.generateSensorStatusExt(sensorStatus, sensorsLabels);
        sensorService.setSelectTypeByStatus(sensorsLabels, sensorStatus);

        String russianMlfb = sensorService.getRussianMlfb(sensor.getMlfb());


//        model.addAttribute("selections", selections);
        model.addAttribute("sensor", sensor);
        model.addAttribute("sensorsLabels", sensorsLabels);
        model.addAttribute("mlfb", Util.separateString(sensor.getMlfb(), 0));
        model.addAttribute("mlfbRus", Util.separateString(russianMlfb, 0));
        model.addAttribute("mlfbB", Util.separateString(sensor.getMlfbB(), 1));
        model.addAttribute("mlfbC", Util.separateString(sensor.getMlfbC(), 2));
        model.addAttribute("sensorStatus", sse);
        if (group!=null) {
            switch (group) {
                case "ZY1":
                    if (id>21) {
                        model.addAttribute("Y01units", Constant.MLFB.NEW_Y01_UNITS);
                        model.addAttribute("Y02units", Constant.MLFB.NEW_Y02_UNITS);
                        model.addAttribute("Y21units", Constant.MLFB.NEW_Y21_UNITS);
                        model.addAttribute("Y22units", Constant.MLFB.NEW_Y22_UNITS);
                        model.addAttribute("Y26units", Constant.MLFB.NEW_Y26_UNITS);
                        model.addAttribute("Y30units", Constant.MLFB.NEW_Y30_UNITS);
                        model.addAttribute("Y31units", Constant.MLFB.NEW_Y31_UNITS);
                        model.addAttribute("Y38units", Constant.MLFB.NEW_Y38_UNITS);
                    } else {//старые датчики
                        model.addAttribute("Y01units", Constant.MLFB.Y01_UNITS);
                    }
                    break;
                case "ZY4":
                    model.addAttribute("Y21units", Constant.MLFB.Y21_UNITS);
                    break;
            }

        }
        System.out.println("*******************************");
        System.out.println(sensor);
        System.out.println(sensorsLabels);
        System.out.println(sensor.getMlfbB());
        System.out.println(Util.separateString(sensor.getMlfbB(), 1));
        System.out.println(sensorStatus);

//        model.addAttribute("mlfb", sensorTypeEntity.getMlfb().toCharArray());
//        model.addAttribute("mlfbB", sensorTypeEntity.getMlfbB());
//        model.addAttribute("mlfbC", sensorTypeEntity.getMlfbC());
//        System.out.println("mlfbB = "+sensorTypeEntity.getMlfbB());
//        System.out.println("mlfbC = "+sensorTypeEntity.getMlfbC());
        return "configuratorlabels";
    }

}
