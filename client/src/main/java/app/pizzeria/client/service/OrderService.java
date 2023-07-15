package app.pizzeria.client.service;

import app.pizzeria.client.model.Order;
import app.pizzeria.client.repository.OrderRepository;
import app.pizzeria.common.model.OrderDto;
import app.pizzeria.common.model.OrderStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private PalmettoNotificationService producer;

    public OrderDto saveOrder(OrderDto orderDto) {
        log.info("Creating a new order '{}'.", orderDto);
        Order entity = convert(orderDto);
        entity.setStatus(OrderStatus.ORDER_CREATED);
        Order savedOrder = repository.save(entity);
        OrderDto savedOrderDto = convert(savedOrder);
        return savedOrderDto;
    }

    public OrderDto updateOrderStatus(OrderDto orderDto) {
        log.info("Updating order status for order '{}'.", orderDto);
        Order order = repository.findById(orderDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Order with id '%s' not found", orderDto.getId())));
        order.setStatus(orderDto.getStatus());
        Order savedOrder = repository.save(order);
        OrderDto savedOrderDto = convert(savedOrder);
        return savedOrderDto;
    }

    public Optional<OrderDto> getOrderById(Long id) {
        log.info("Searching order by id '{}'.", id);
        return repository.findById(id).map(this::convert);
    }

    public List<OrderDto> getOrders() {
        log.info("Searching all orders.");
        return repository.findAll().stream().map(this::convert).collect(Collectors.toList());
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
                .status(orderDto.getStatus())
                .build();
    }

    private OrderDto convert(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .orderItem(order.getOrderItem())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .build();
    }

}
