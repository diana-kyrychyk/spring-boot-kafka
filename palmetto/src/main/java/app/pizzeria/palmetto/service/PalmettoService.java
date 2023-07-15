package app.pizzeria.palmetto.service;

import app.pizzeria.common.model.NotificationDto;
import app.pizzeria.common.model.OrderDto;
import app.pizzeria.common.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PalmettoService {

    private final CourierNotificationService producer;

    @Autowired
    public PalmettoService(CourierNotificationService producer) {
        this.producer = producer;
    }

    public void processOrder(OrderDto order) {
        NotificationDto notification = new NotificationDto();
        notification.setOrder(order);
        notification.setMessage(String.format("Order for '%s' is created.", order.getOrderItem()));
        notify(notification);
    }

    private void notify(NotificationDto notification) {
        notification.getOrder().setStatus(OrderStatus.ORDER_IS_READY);
        producer.notify(notification);
    }

}
