package com.nppgks.dkipia.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nppgks.dkipia.entity.*;
import com.nppgks.dkipia.service.DataService;
import com.nppgks.dkipia.service.SensorService;
import com.nppgks.dkipia.util.Constant;
import com.nppgks.dkipia.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
public class WebController {

    @Autowired
    private SensorService sensorService;

    @Autowired
    private DataService dataService;

    @GetMapping("/")
    public String goToConfigurator() {
        return "redirect:configurator";
    }

    @RequestMapping("/configurator")
    public String getSensorType(Model model) {
        List<Sensors> sensors = sensorService.getSensors();
        model.addAttribute("sensors", sensors);
        return "configurator";
    }

    @GetMapping("/basket")
    public String getBasket(Model model, HttpServletRequest request) {
        log.info("GetMapping getBasket");
        List<String> mlfbList = dataService.getDataList(request.getSession().getId());
        List<SensorFull> sensorFullList = sensorService.getSensorFullList(mlfbList);

        //Список комплектующих
        List<Complete> completeList = sensorService.getComplete(true);

        model.addAttribute("comletes", completeList);
        model.addAttribute("sensorfull", sensorFullList);
        return "basket";
    }

    @GetMapping("/settings")
    public String getSettings(Model model) {
        log.info("GetMapping getSettings");
        //Список комплектующих
        List<Complete> completeList = sensorService.getComplete(false);
        model.addAttribute("comletes", completeList);

        return "settings";
    }

    @PostMapping("/addtobasket")
    public ResponseEntity<String> addtobasket(@RequestBody String payload, HttpServletRequest request) {
        String mlfb = Util.convertStringFromJson(payload);
        log.info("mlfb = " + mlfb);
        dataService.insertData(request.getSession().getId(), mlfb);
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/removefrombasket")
    public ResponseEntity<String> removefrombasket(@RequestBody String payload, HttpServletRequest request) {
        log.info("PostMapping removefrombasket");
        String mlfb = Util.convertStringFromJson(payload);
        log.info("mlfb = " + mlfb);
        dataService.removeData(request.getSession().getId(), mlfb);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/configurator/{id}")
    public String getConfigurator(@PathVariable int id, Model model) {
        log.info("GetMapping getConfigurator");
        Sensors sensor = sensorService.getSensorById(id);
        List<SensorsLabels> sensorsLabels = sensorService.getSensorLabels(id);
        if (id <= 21) {
            sensorService.setActiveLabelByPosition(sensorsLabels, "8");
        } else {
            sensorService.setActiveLabelByPosition(sensorsLabels, "6");
        }
        SensorStatusExtension sse = Util.generateSensorStatusExt(null, null);
        String russianMlfb = sensorService.getRussianMlfb(sensor.getMlfbstandart());
        log.info(sensor.toString());
        log.info(sensorsLabels.toString());
        model.addAttribute("sensor", sensor);
        model.addAttribute("sensorsLabels", sensorsLabels);
        model.addAttribute("mlfb", Util.separateString(sensor.getMlfbstandart(), 0));
        model.addAttribute("mlfbB", null);
        model.addAttribute("mlfbC", null);
        model.addAttribute("mlfbRus", Util.separateString(russianMlfb, 0));
        model.addAttribute("sensorStatus", sse);
        return "configuratorlabels";
    }

    @PostMapping("/configurator/{id}")
    public String getConfigurator(@PathVariable int id, @RequestParam String mlfb, @RequestParam String mlfbB, @RequestParam String mlfbC, @RequestParam String group, @RequestParam String option, @RequestParam String mlfbCText, @RequestParam int isformat, Model model) {
        log.info("PostMapping getConfigurator");
        log.info("mlfb = " + mlfb);
        log.info("mlfbB = " + mlfbB);
        log.info("mlfbC = " + mlfbC);
        log.info("mlfbCText = " + mlfbCText);
        log.info("option = " + option);
        log.info("group = " + group);
        log.info("isformat = " + isformat);

        Sensors sensor = sensorService.getSensorById(id);
        //формируем необходимые label
        List<SensorsLabels> sensorsLabels = sensorService.getSensorLabels(id);
        //выбираем только необходимую опции у label, а не все параметры
        sensorService.setActiveLabelByPosition(sensorsLabels, group);

        if (isformat == 1) {
            List<String> separateList = sensorService.getSeparateMlfb(mlfb);
            if (separateList != null) {
                mlfb = separateList.get(0);
                mlfbB = separateList.get(1);
                mlfbC = separateList.get(2);
                log.info("updated mlfb = " + mlfb);
                log.info("updated mlfbB = " + mlfbB);
                log.info("updated mlfbC = " + mlfbC);
            }
        }
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

        List<SensorStatus> sensorStatus = sensorService.getSensorStatus(fullMlfb);
        SensorStatusExtension sse = Util.generateSensorStatusExt(sensorStatus, sensorsLabels);
        sensorService.setSelectTypeByStatus(sensorsLabels, sensorStatus);
        String russianMlfb = sensorService.getRussianMlfb(sensor.getMlfb());

        model.addAttribute("sensor", sensor);
        model.addAttribute("sensorsLabels", sensorsLabels);
        model.addAttribute("mlfb", Util.separateString(sensor.getMlfb(), 0));
        model.addAttribute("mlfbRus", Util.separateString(russianMlfb, 0));
        model.addAttribute("mlfbB", Util.separateString(sensor.getMlfbB(), 1));
        model.addAttribute("mlfbC", Util.separateString(sensor.getMlfbC(), 2));
        model.addAttribute("sensorStatus", sse);
        if (group != null) {
            switch (group) {
                case "ZY1":
                    if (id > 21) {
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

        log.info("*******************************");
        log.info("fullMlfb = " + fullMlfb);
        log.info("sensor = " + sensor.toString());
        log.info("sensorsLabels = " + sensorsLabels.toString());
        log.info("MlfbB = " + sensor.getMlfbB());
        log.info("sensorStatus = " + sensorStatus.toString());
        log.info("*******************************");
        return "configuratorlabels";
    }

    @RequestMapping("/savecomplete")
    public ResponseEntity<String> sendMailWithAttachment(@RequestBody String payload) throws JsonProcessingException {
        log.info("RequestMapping sendMailWithAttachment");
        log.info(payload);
        ObjectMapper mapper = new ObjectMapper();
        List<Complete> completeList = mapper.readValue(payload, new TypeReference<List<Complete>>() {
        });
        if (completeList!=null) {
            if (sensorService.saveComplete(completeList)) {
                return ResponseEntity.ok("ok");
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }

}
