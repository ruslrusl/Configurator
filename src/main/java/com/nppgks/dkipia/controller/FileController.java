package com.nppgks.dkipia.controller;


import com.nppgks.dkipia.entity.SensorFull;
import com.nppgks.dkipia.entity.outside.Jobject;
import com.nppgks.dkipia.service.DataService;
import com.nppgks.dkipia.service.ExcelService;
import com.nppgks.dkipia.service.SensorService;
import com.nppgks.dkipia.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private SensorService sensorService;

    @Autowired
    private DataService dataService;

    @RequestMapping("/getfile")
    public ResponseEntity<Resource> getFile(@RequestBody String payload) throws IOException {
        log.info("FileController getfile");
        log.info("payload = " + payload);
        Jobject jobject = excelService.convertFromJson(payload);
        if (jobject != null) {
            String fileName = excelService.generateFile(jobject.getSensors(), jobject.getType() != null ? jobject.getType().get(0) : 1, jobject.getNumber());
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


    @RequestMapping("/downloadtobasket")
    public ResponseEntity<Jobject> downloadToBasket(@RequestParam("downloadfile") final MultipartFile file, HttpServletRequest request) {

        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
        final Path path = Paths.get(Util.getDir(), file.getOriginalFilename());
        final byte[] bytes;
        try {
            bytes = file.getBytes();
            Files.write(path, bytes);

            Jobject jobject = excelService.generateObjectFromFile(Util.getDir() + file.getOriginalFilename());
            jobject.setNumber(getNumber(file.getOriginalFilename()));

            if (jobject != null) {
                if (jobject.getSensors() != null) {
                    jobject.getSensors().forEach(j -> {
                        dataService.insertData(request.getSession().getId(), j.getMlfbrus());
                        SensorFull sensorFull = sensorService.getSensorFullList(j.getMlfbrus());
                        if (sensorFull != null) {
                            j.setMlfb(sensorFull.getMlfb());
                            j.setDescr(sensorFull.getDescr());
                            j.setPrice(sensorFull.getPrice());
                        }
                    });
                }
            }
            return ResponseEntity.ok()
                    .body(jobject);
        } catch (IOException e) {
            log.error("", e);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private int getNumber(String fileName) {
        String str = StringUtils.substringBefore(fileName, "_");
        int foo;
        try {
            foo = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            foo = 0;
        }
        return foo;
    }
}
