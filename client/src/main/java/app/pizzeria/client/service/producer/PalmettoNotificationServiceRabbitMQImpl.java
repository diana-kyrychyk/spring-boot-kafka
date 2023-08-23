package app.pizzeria.client.service.producer;

import app.pizzeria.common.model.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("rabbitmq")
public class PalmettoNotificationServiceRabbitMQImpl implements PalmettoNotificationService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;

    @Autowired
    public PalmettoNotificationServiceRabbitMQImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void notify(OrderDto order) {
        log.info("Notifying Palmetto about the new order: '{}' from '{}'.", order.getOrderItem(), order.getOrderDate());
        rabbitTemplate.convertAndSend("", routingkey, order);
    }

}
