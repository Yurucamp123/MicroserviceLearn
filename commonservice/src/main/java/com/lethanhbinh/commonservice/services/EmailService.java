package com.lethanhbinh.commonservice.services;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private Configuration config;

    public void sendEmail(String to, String subject, String text, boolean isHtml, File attachment) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, isHtml);
            if (attachment != null) {
                FileSystemResource fileSystemResource = new FileSystemResource(attachment);
                helper.addAttachment(fileSystemResource.getFilename(), attachment);
            }

            emailSender.send(message);
            log.info("Email sent successfully to {}", to);

        } catch (MessagingException ex) {
            log.error("Failed to send email to {}", to, ex);

            // handle exception (retry logic, save to ltq)

        }
    }

    public void sendEmailWithTemplate(String to, String subject,
                                      String templateName, Map<String, Object> placeholder, File attachment) {
        try {
            Template template = config.getTemplate(templateName);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, placeholder);

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            if (attachment != null) {
                FileSystemResource fileSystemResource = new FileSystemResource(attachment);
                helper.addAttachment(fileSystemResource.getFilename(), attachment);
            }

            emailSender.send(message);
            log.info("Email sent successfully to {}", to);
        } catch (MessagingException |IOException | TemplateException ex) {
            log.error("Failed to send email to {}", to, ex);
        }
    }
}
