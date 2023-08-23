package app.pizzeria.courier.service.consumer;

import app.pizzeria.common.model.NotificationDto;
import app.pizzeria.common.model.OrderStatus;
import app.pizzeria.courier.service.CourierService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    private CourierService courierService;

    @KafkaListener(topics = {"Notification"})
    public void consumeNotificationMessage(NotificationDto notification) {
        log.info("Received notification from Palmetto, order status: '{}'.", notification.getOrder().getStatus());
        System.out.println("Received notification from Palmetto, order status: " + notification.getMessage());
        if (OrderStatus.ORDER_IS_READY.name().equals(notification.getOrder().getStatus().name())) {
            courierService.processNotification(notification);
        }
    }

}
