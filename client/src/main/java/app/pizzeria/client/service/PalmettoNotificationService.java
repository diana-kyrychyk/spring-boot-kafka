package app.pizzeria.client.service;

import app.pizzeria.common.model.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PalmettoNotificationService {

    private final String topic;

    private final KafkaTemplate<String, OrderDto> kafkaTemplate;

    @Autowired
    public PalmettoNotificationService(KafkaTemplate<String, OrderDto> kafkaTemplate, @Value("${kafka.producer.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void notify(OrderDto order) {
        kafkaTemplate.send(topic, String.valueOf(order.getId()), order);
    }

}
