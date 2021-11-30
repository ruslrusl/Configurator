package com.nppgks.dkipia.controller;

import com.nppgks.dkipia.entity.outside.Jobject;
import com.nppgks.dkipia.service.ExcelService;
import com.nppgks.dkipia.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class EmailController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ExcelService excelService;

    @RequestMapping("/sendmail")
    public ResponseEntity<String> sendMailWithAttachment(@RequestBody String payload) {
        Jobject jobject = excelService.convertFromJson(payload);
        if (jobject != null) {
            List<String> list = new ArrayList<>();
            for (int type : jobject.getType()) {
                String fileName = excelService.generateFile(jobject.getSensors(), type);
                list.add(fileName);
            }
            if (list.size() > 0) {
                MimeMessagePreparator preparator = mimeMessage -> {
                    mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(jobject.getSendto()));
                    mimeMessage.setFrom(new InternetAddress(Constant.MAIL.FROM));
                    mimeMessage.setSubject("Выгрузка из конфигуратора");
                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

                    for (String fileName : list) {
                        log.info("Прикрепить файл: "+fileName);
                        File file = new File(fileName);
                        helper.addAttachment(file.getName(), new ByteArrayResource(IOUtils.toByteArray(FileUtils.openInputStream(file))));
                    }
                    helper.setText("Данное письмо сформировано автоматически и не требует ответа", true);
                };

                try {
                    mailSender.send(preparator);
                    return ResponseEntity.ok("ok");
                } catch (MailException ex) {
                    log.error("Ошибка при отправке", ex);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }
}
