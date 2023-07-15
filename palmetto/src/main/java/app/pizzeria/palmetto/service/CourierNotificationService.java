package app.pizzeria.palmetto.service;

import app.pizzeria.common.model.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CourierNotificationService {

    private final String topic;

    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;

    @Autowired
    public CourierNotificationService(KafkaTemplate<String, NotificationDto> kafkaTemplate, @Value("${kafka.producer.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void notify(NotificationDto notification) {
        log.info("Palmetto app notifies Courier app");
        kafkaTemplate.send(topic, String.valueOf(notification.getOrder().getId()), notification);
    }

}
