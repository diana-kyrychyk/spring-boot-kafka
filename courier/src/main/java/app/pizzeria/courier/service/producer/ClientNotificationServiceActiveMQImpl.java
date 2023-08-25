package app.pizzeria.courier.service.producer;

import app.pizzeria.common.model.NotificationDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("activemq")
public class ClientNotificationServiceActiveMQImpl implements ClientNotificationService {

    private final String topic;

    private final JmsTemplate jmsTemplate;

    @Autowired
    public ClientNotificationServiceActiveMQImpl(@Value("${activemq.producer.queue}") String topic, JmsTemplate jmsTemplate) {
        this.topic = topic;
        this.jmsTemplate = jmsTemplate;
    }

    public void notify(NotificationDto notification) {
        try {
            log.info("Notifying Client app about the new order status '{}' for order: '{}' from '{}'.", notification.getOrder().getStatus(), notification.getOrder().getOrderItem(), notification.getOrder().getOrderDate());
            String notificationMsg = new ObjectMapper().writeValueAsString(notification);
            jmsTemplate.convertAndSend(topic, notificationMsg);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
