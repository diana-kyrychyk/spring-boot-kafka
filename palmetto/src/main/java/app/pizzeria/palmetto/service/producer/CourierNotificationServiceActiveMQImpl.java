package app.pizzeria.palmetto.service.producer;

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
public class CourierNotificationServiceActiveMQImpl implements CourierNotificationService {

    private final JmsTemplate jmsTemplate;

    private final String topic;

    @Autowired
    public CourierNotificationServiceActiveMQImpl(@Value("${activemq.producer.queue}") String topic, JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
        this.topic = topic;
    }

    @Override
    public void notify(NotificationDto notification) {
        try {
            log.info("Notifying Courier app about the new order status '{}' for order: '{}' from '{}'.", notification.getOrder().getStatus(), notification.getOrder().getOrderItem(), notification.getOrder().getOrderDate());
            String notificationMsg = new ObjectMapper().writeValueAsString(notification);
            jmsTemplate.convertAndSend(topic, notificationMsg);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
