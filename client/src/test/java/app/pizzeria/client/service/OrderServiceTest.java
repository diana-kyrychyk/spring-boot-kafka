package app.pizzeria.client.service;

import app.pizzeria.client.model.Order;
import app.pizzeria.client.repository.OrderRepository;
import app.pizzeria.client.service.producer.PalmettoNotificationServiceKafkaImpl;
import app.pizzeria.common.model.OrderDto;
import app.pizzeria.common.model.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private PalmettoNotificationServiceKafkaImpl producer;

    @InjectMocks
    private OrderService orderService;


    @Test
    void shouldReturnSavedOrderDtoWithCreatedStatus_WhenSaveOrder() {
        OrderDto orderDto = createOrderDto();
        Order savedOrder = createOrder();
        when(repository.save(Mockito.any(Order.class))).thenReturn(savedOrder);

        OrderDto result = orderService.saveOrder(orderDto);

        assertNotNull(result);
        assertEquals(OrderStatus.ORDER_CREATED, result.getStatus());
    }

    @Test
    void shouldReturnOrderDto_WhenGetOrderByIdAndOrderExists() {
        Long orderId = 1L;
        Order order = createOrder();
        when(repository.findById(orderId)).thenReturn(Optional.of(order));

        Optional<OrderDto> result = orderService.getOrderById(orderId);

        assertTrue(result.isPresent());
        assertEquals(orderId, result.get().getId());

    }

    @Test
    void shouldReturnEmptyOptional_WhenGetOrderByIdAndOrderNotExists() {
        Long orderId = 1L;
        when(repository.findById(orderId)).thenReturn(Optional.empty());

        Optional<OrderDto> result = orderService.getOrderById(orderId);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldNotifyPalmetto() {
        OrderDto orderDto = createOrderDto();

        orderService.notifyPalmetto(orderDto);

        verify(producer).notify(orderDto);
    }

    private OrderDto createOrderDto() {
        return OrderDto.builder()
                .id(1L)
                .customerName("Hryhorii M.")
                .orderItem("Pizza Hawaii")
                .orderDate("2023-07-07")
                .totalAmount(1)
                .build();
    }

    private Order createOrder() {
        return Order.builder()
                .id(1L)
                .customerName("Hryhorii M.")
                .orderItem("Pizza Hawaii")
                .orderDate("2023-07-07")
                .totalAmount(1)
                .build();
    }
}