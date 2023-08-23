package app.pizzeria.palmetto.service.consumer;

import app.pizzeria.common.model.OrderDto;
import app.pizzeria.palmetto.service.PalmettoService;
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
    private PalmettoService service;

    @JmsListener(destination = "${activemq.consumer.queue}")
    public void consumeOrderMessage(String orderMsg) throws JsonProcessingException {
        OrderDto order = new ObjectMapper().readValue(orderMsg, OrderDto.class);
        log.info("Palmetto received order: '{}' from '{}' on '{}'.", order.getOrderItem(), order.getCustomerName(), order.getOrderDate());
        String msg = String.format("Palmetto received order: '%s' from '%s' on '%s'.", order.getOrderItem(), order.getCustomerName(), order.getOrderDate());
        System.out.println(msg);
        service.processOrder(order);
    }

}
