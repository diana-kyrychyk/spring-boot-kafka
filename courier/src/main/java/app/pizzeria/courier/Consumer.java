package app.pizzeria.courier;

import app.pizzeria.courier.model.Notification;
import app.pizzeria.common.model.OrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @KafkaListener(topics = {"Order"})
    public void consumeOrderMessage(OrderDto order) {
        log.info("Received order message: {}", order);
        System.out.println("Received order message: " + order.toString());
    }

    @KafkaListener(topics = {"Notification"})
    public void consumeNotificationMessage(String notificationStr) {
        //TODO create NotificationDto - no need for ObjectMapper usage
        Notification notification = convertStringToObject(notificationStr, Notification.class);
        System.out.println("Received notification message: " + notification.getMessage());
    }

    private <T> T convertStringToObject(String orderStr, Class<T> cls) {
        try {
            return MAPPER.readValue(orderStr, cls);
        } catch (JsonProcessingException e) {
            log.error("Error converting string to order object");
            throw new RuntimeException(e);
        }
    }

}
