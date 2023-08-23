package app.pizzeria.palmetto.service.producer;

import app.pizzeria.common.model.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("kafka")
public class CourierNotificationServiceKafkaImpl implements CourierNotificationService {

    private final String topic;

    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;

    @Autowired
    public CourierNotificationServiceKafkaImpl(KafkaTemplate<String, NotificationDto> kafkaTemplate, @Value("${kafka.producer.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void notify(NotificationDto notification) {
        log.info("Notifying Courier app about the new order status '{}' for order: '{}' from '{}'.", notification.getOrder().getStatus(), notification.getOrder().getOrderItem(), notification.getOrder().getOrderDate());
        kafkaTemplate.send(topic, String.valueOf(notification.getOrder().getId()), notification);
    }

}
