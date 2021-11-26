package com.nppgks.dkipia.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nppgks.dkipia.entity.Sensors;
import com.nppgks.dkipia.entity.outside.Jobject;
import com.nppgks.dkipia.entity.outside.Jsensor;
import com.nppgks.dkipia.service.ExcelService;
import com.nppgks.dkipia.service.SensorService;
import com.nppgks.dkipia.util.Constant;
import com.nppgks.dkipia.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Controller
public class FileController {

    @Autowired
    private ExcelService excelService;

    @RequestMapping( "/getfile" )
    public ResponseEntity<Resource> getFile(@RequestBody String payload) throws IOException  {
        Jobject jobject =excelService.convertFromJson(payload);
        String fileName = excelService.generateFile(jobject);
        log.info("payload = "+payload);
        log.info("fileName = "+fileName);
        if (fileName!=null) {
            File file = new File(fileName);
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+file.getName());
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");

            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

            return ResponseEntity.ok()
                    .headers(header)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(resource);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
