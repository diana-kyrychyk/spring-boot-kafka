package app.pizzeria.courier;

import app.pizzeria.common.model.NotificationDto;
import app.pizzeria.common.model.OrderStatus;
import app.pizzeria.courier.service.CourierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer {

    @Autowired
    private CourierService courierService;

    @KafkaListener(topics = {"Notification"})
    public void consumeNotificationMessage(NotificationDto notification) {
        log.info("Received notification message: {}.", notification);
        System.out.println("Received notification message: " + notification.getMessage());
        if (OrderStatus.PIZZA_IS_READY.name().equals(notification.getOrder().getStatus().name())) {
            courierService.processNotification(notification);
        }
    }

}
