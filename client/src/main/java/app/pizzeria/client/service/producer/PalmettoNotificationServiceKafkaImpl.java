package app.pizzeria.client.service.producer;

import app.pizzeria.common.model.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("kafka")
public class PalmettoNotificationServiceKafkaImpl implements PalmettoNotificationService {

    private final String topic;

    private final KafkaTemplate<String, OrderDto> kafkaTemplate;

    @Autowired
    public PalmettoNotificationServiceKafkaImpl(KafkaTemplate<String, OrderDto> kafkaTemplate, @Value("${kafka.producer.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void notify(OrderDto order) {
        log.info("Notifying Palmetto about the new order: '{}' from '{}'.", order.getOrderItem(), order.getOrderDate());
        kafkaTemplate.send(topic, String.valueOf(order.getId()), order);
    }

}
