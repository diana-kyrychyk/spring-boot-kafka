package app.pizzeria.client.controller;

import app.pizzeria.client.service.OrderService;
import app.pizzeria.common.model.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    private final String topic;

    private final KafkaTemplate<String, OrderDto> kafkaTemplate;


    @Autowired
    public OrderController(OrderService service, @Value("${kafka.producer.topic}") String topic, KafkaTemplate<String, OrderDto> kafkaTemplate) {
        this.orderService = service;
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("")
    public ResponseEntity<String> createOrder(@RequestBody OrderDto order) {
        OrderDto savedOrder = orderService.saveOrder(order);
        notifyCourier(savedOrder);
        return ResponseEntity.ok(String.format("Order for '%s' created successfully.", savedOrder.getOrderItem()));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long orderId) {
        Optional<OrderDto> orderOpt = orderService.getOrderById(orderId);
        return orderOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    private void notifyCourier(OrderDto order) {
        kafkaTemplate.send(topic, order.getId().toString(), order);
    }
}
