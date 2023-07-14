package app.pizzeria.courier.service;

import app.pizzeria.common.model.NotificationDto;
import app.pizzeria.common.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Consumer {

    @Autowired
    private CourierService courierService;

    @KafkaListener(topics = {"Notification"})
    public void consumeNotificationMessage(NotificationDto notification) {
        log.info("Received notification message: {}.", notification);
        System.out.println("Received notification message: " + notification.getMessage());
        if (OrderStatus.ORDER_IS_READY.name().equals(notification.getOrder().getStatus().name())) {
            courierService.processNotification(notification);
        }
    }

}
