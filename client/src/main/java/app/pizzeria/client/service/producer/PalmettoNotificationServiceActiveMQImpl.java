package app.pizzeria.client.service.producer;

import app.pizzeria.common.model.OrderDto;
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
public class PalmettoNotificationServiceActiveMQImpl implements PalmettoNotificationService {

    private final String topic;

    private final JmsTemplate jmsTemplate;

    @Autowired
    public PalmettoNotificationServiceActiveMQImpl(@Value("${activemq.producer.queue}") String topic, JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
        this.topic = topic;
    }

    @Override
    public void notify(OrderDto order) {
        try {
            log.info("Notifying Palmetto about the new order: '{}' from '{}'.", order.getOrderItem(), order.getOrderDate());
            String orderMsg = new ObjectMapper().writeValueAsString(order);
            jmsTemplate.convertAndSend(topic, orderMsg);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
