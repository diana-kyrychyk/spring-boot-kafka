package app.pizzeria.client.service.consumer;

import app.pizzeria.client.service.OrderService;
import app.pizzeria.common.model.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("kafka")
public class ConsumerKafkaImpl {

    @Autowired
    private OrderService service;

    @KafkaListener(topics = {"Notification"})
    public void consumeNotificationMessage(NotificationDto notification) {
        log.info("Client app received notification, order status: '{}'.", notification.getOrder().getStatus());
        System.out.println("Client app received notification, order status: " + notification.getMessage());
        service.updateOrderStatus(notification.getOrder());
    }

}
