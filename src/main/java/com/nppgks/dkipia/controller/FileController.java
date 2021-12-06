package com.nppgks.dkipia.controller;


import com.nppgks.dkipia.entity.outside.Jobject;
import com.nppgks.dkipia.service.ExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Controller
public class FileController {

    @Autowired
    private ExcelService excelService;

    @RequestMapping("/getfile")
    public ResponseEntity<Resource> getFile(@RequestBody String payload) throws IOException {
        log.info("FileController getfile");
        log.info("payload = " + payload);
        Jobject jobject = excelService.convertFromJson(payload);
        if (jobject != null) {
            String fileName = excelService.generateFile(jobject.getSensors(), jobject.getType() != null ? jobject.getType().get(0) : 1, jobject.getNumber());
            log.info("payload = " + payload);
            log.info("fileName = " + fileName);
            if (fileName != null) {
                File file = new File(fileName);
                HttpHeaders header = new HttpHeaders();
                header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.name()));
                header.add("Cache-Control", "no-cache, no-store, must-revalidate");
                header.add("Pragma", "no-cache");
                header.add("Expires", "0");

                Path path = Paths.get(file.getAbsolutePath());
                ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

                file.deleteOnExit();

                return ResponseEntity.ok()
                        .headers(header)
                        .contentLength(file.length())
                        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                        .body(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }
}
