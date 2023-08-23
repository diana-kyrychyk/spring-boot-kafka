package app.pizzeria.courier.service.producer;

import app.pizzeria.common.model.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("rabbitmq")
public class ClientNotificationServiceRabbitMQImpl implements ClientNotificationService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;

    @Autowired
    public ClientNotificationServiceRabbitMQImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void notify(NotificationDto notification) {
        log.info("Notifying Client app about the new order status '{}' for order: '{}' from '{}'.", notification.getOrder().getStatus(), notification.getOrder().getOrderItem(), notification.getOrder().getOrderDate());
        rabbitTemplate.convertAndSend("", routingkey, notification);
    }

}