package app.pizzeria.courier.service;

import app.pizzeria.common.model.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ClientNotificationService {

    private final String topic;

    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;

    @Autowired
    public ClientNotificationService(KafkaTemplate<String, NotificationDto> kafkaTemplate, @Value("${kafka.producer.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void notify(NotificationDto notification) {
        kafkaTemplate.send(topic, String.valueOf(notification.getOrder().getId()), notification);
    }

}
