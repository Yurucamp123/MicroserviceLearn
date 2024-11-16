package com.lethanhbinh.notificationservice.event;

import com.lethanhbinh.commonservice.services.EmailService;
import com.lethanhbinh.notificationservice.data.EmailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.RetriableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class EventConsumer {

    @Autowired
    private EmailService emailService;

    @RetryableTopic(
            // 3 topics retry + 1 dead letter queue
            attempts = "4",
            // delay 1 giây, sau mỗi lần thử thời gian tăng gấp đôi
            backoff = @Backoff(delay = 1000, multiplier = 2),
            autoCreateTopics = "true",
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            include = {
                    RetriableException.class, RuntimeException.class
            }
    )
    @KafkaListener(topics = "test", containerFactory = "kafkaListenerContainerFactory")
    public void listen(String message) {
        log.info("Receive Message: {}", message);

        // test message fail
        throw new RuntimeException("Error Test");
    }

    @DltHandler
    public void handleDltMessage(@Payload String message) {
        log.info("DLT receives message: {}", message);
    }

    @KafkaListener(topics = "testEmail", containerFactory = "kafkaListenerContainerFactory")
    public void testEmail(String message) {
        log.info("Receive Message: {}", message);

        String template = "<div>\n" +
                "    <h1>Welcome, %s!</h1>\n" +
                "    <p>Thank you for join us. We are excited to have you on board</p>\n" +
                "    <p>Your username is: <strong>%s</strong></p>\n" +
                "</div>";

        String fileTemplate = String.format(template, "Thanh Binh", message);
        emailService.sendEmail(message, "Thank to buy my course", fileTemplate, true, null);
    }

    @KafkaListener(topics = "EmailTemplate", containerFactory = "kafkaListenerContainerFactory")
    public void emailTemplate (EmailTemplate emailTemplate) {
        Map<String, Object> placeholders = new HashMap<>();
        placeholders.put("name", "lethanhbinh");

        String message = emailTemplate.getMessage();
        String to = emailTemplate.getEmailTo();

        log.info("Receive Message: {}", message);
        emailService.sendEmailWithTemplate(to, "Thank to buy my course", "EmailTemplate.ftl", placeholders, null);

    }
}
