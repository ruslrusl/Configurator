package com.nppgks.dkipia.controller;

import com.nppgks.dkipia.entity.Price;
import com.nppgks.dkipia.entity.start.SModel;
import com.nppgks.dkipia.entity.start.Signal;
import com.nppgks.dkipia.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JsonController {

    @Autowired
    private SensorService sensorService;

    @GetMapping("/getprice/{id}")
    public List<Price> getPrice(@PathVariable int id) {
        List<Price> priceList = sensorService.getPrice(id);
        return priceList;
    }

    @GetMapping("/getmodel/{id}")
    public List<SModel> getModel(@PathVariable int id) {
        List<SModel> sModelList = sensorService.getModelForStart(id);
        return sModelList;
    }

    @GetMapping("/getsignal/{id}/{sid}")
    public List<Signal> getSignal(@PathVariable int id, @PathVariable int sid) {
        List<Signal> signalList = sensorService.getSignalForStart(id, sid);
        return signalList;
    }

}
