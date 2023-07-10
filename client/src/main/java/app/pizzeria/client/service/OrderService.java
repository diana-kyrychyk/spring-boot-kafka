package app.pizzeria.client.service;

import app.pizzeria.client.PalmettoNotificationService;
import app.pizzeria.client.model.Order;
import app.pizzeria.client.repository.OrderRepository;
import app.pizzeria.common.model.OrderDto;
import app.pizzeria.common.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private PalmettoNotificationService producer;

    public OrderDto saveOrder(OrderDto orderDto) {
        log.info("Creating a new orderDto '{}'.", orderDto.getOrderItem());
        Order entity = convert(orderDto);
        Order savedOrder = repository.save(entity);
        OrderDto savedOrderDto = convert(savedOrder);
        savedOrderDto.setStatus(OrderStatus.ORDER_CREATED);
        return savedOrderDto;
    }

    public Optional<OrderDto> getOrderById(Long id) {
        log.info("Searching order by id '{}'.", id);
        return repository.findById(id).map(this::convert);
    }

    public void notifyPalmetto(OrderDto order) {
        producer.notify(order);
    }

    private Order convert(OrderDto orderDto) {
        return Order.builder()
                .id(orderDto.getId())
                .customerName(orderDto.getCustomerName())
                .orderItem(orderDto.getOrderItem())
                .orderDate(orderDto.getOrderDate())
                .totalAmount(orderDto.getTotalAmount())
                .build();
    }

    private OrderDto convert(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .orderItem(order.getOrderItem())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .build();
    }

}
