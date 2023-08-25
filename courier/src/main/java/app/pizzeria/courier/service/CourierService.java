package app.pizzeria.courier.service;

import app.pizzeria.common.model.NotificationDto;
import app.pizzeria.common.model.OrderStatus;
import app.pizzeria.courier.service.producer.ClientNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CourierService {

    private final ClientNotificationService producer;

    @Autowired
    public CourierService(ClientNotificationService producer) {
        this.producer = producer;
    }

    public void processNotification(NotificationDto notification) {
        notify(notification);
    }

    private void notify(NotificationDto notification) {
        NotificationDto updatedNotification = new NotificationDto();
        updatedNotification.setMessage("Pizza delivered");
        updatedNotification.setOrder(notification.getOrder());
        updatedNotification.getOrder().setStatus(OrderStatus.ORDER_DELIVERED);
        producer.notify(updatedNotification);
    }

}
