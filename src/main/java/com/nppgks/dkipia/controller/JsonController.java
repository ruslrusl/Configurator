package com.nppgks.dkipia.controller;

import com.nppgks.dkipia.entity.Price;
import com.nppgks.dkipia.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
