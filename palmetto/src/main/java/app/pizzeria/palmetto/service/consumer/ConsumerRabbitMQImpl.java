package app.pizzeria.palmetto.service.consumer;

import app.pizzeria.common.model.OrderDto;
import app.pizzeria.palmetto.service.PalmettoService;
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
    private PalmettoService service;

    @RabbitListener(queues = {"${rabbitmq.consumer.queue}"})
    public void consumeOrderMessage(OrderDto order) {
        log.info("Palmetto received order: '{}' from '{}' on '{}'.", order.getOrderItem(), order.getCustomerName(), order.getOrderDate());
        String msg = String.format("Palmetto received order: '%s' from '%s' on '%s'.", order.getOrderItem(), order.getCustomerName(), order.getOrderDate());
        System.out.println(msg);
        service.processOrder(order);
    }

}
