package app.pizzeria.client.service.consumer;

import app.pizzeria.client.service.OrderService;
import app.pizzeria.common.model.NotificationDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("activemq")
public class ConsumerActiveMQImpl {

    @Autowired
    private OrderService service;

    @JmsListener(destination = "${activemq.consumer.queue}")
    public void consumeNotificationMessage(String notificationMsg) throws JsonProcessingException {
        NotificationDto notification = new ObjectMapper().readValue(notificationMsg, NotificationDto.class);
        log.info("Client app received notification, order status: '{}'.", notification.getOrder().getStatus());
        System.out.println("Client app received notification, order status: " + notification.getMessage());
        service.updateOrderStatus(notification.getOrder());
    }

}
