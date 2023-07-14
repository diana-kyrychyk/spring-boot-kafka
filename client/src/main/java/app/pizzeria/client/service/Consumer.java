package app.pizzeria.client.service;

import app.pizzeria.common.model.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Consumer {

    @Autowired
    private OrderService service;

    @KafkaListener(topics = {"Notification"})
    public void consumeNotificationMessage(NotificationDto notification) {
        log.info("Received notification message: {}.", notification);
        System.out.println("Received notification message: " + notification.getMessage());
        service.updateOrderStatus(notification.getOrder());
    }

}
