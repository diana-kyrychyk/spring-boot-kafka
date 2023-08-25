package app.pizzeria.courier.service.consumer;

import app.pizzeria.common.model.NotificationDto;
import app.pizzeria.common.model.OrderStatus;
import app.pizzeria.courier.service.CourierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("rabbitmq")
public class ConsumerRabbitMQImpl {

    @Autowired
    private CourierService courierService;

    @RabbitListener(queues = {"${rabbitmq.consumer.queue}"})
    public void consumeNotificationMessage(NotificationDto notification) {
        log.info("Received notification from Palmetto, order status: '{}'.", notification.getOrder().getStatus());
        System.out.println("Received notification from Palmetto, order status: " + notification.getMessage());
        if (OrderStatus.ORDER_IS_READY.name().equals(notification.getOrder().getStatus().name())) {
            courierService.processNotification(notification);
        }
    }

}
