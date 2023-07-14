package app.pizzeria.palmetto.service;

import app.pizzeria.common.model.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Consumer {

    @Autowired
    private PalmettoService service;

    @KafkaListener(topics = {"${kafka.consumer.topic}"})
    public void consumeOrderMessage(OrderDto order) {
        log.info("Received order message: {}.", order);
        System.out.println("Received order message: " + order.toString());
        service.processOrder(order);
    }

}
