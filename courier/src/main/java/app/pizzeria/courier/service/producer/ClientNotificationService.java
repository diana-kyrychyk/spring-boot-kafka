package app.pizzeria.courier.service.producer;

import app.pizzeria.common.model.NotificationDto;

public interface ClientNotificationService {

    void notify(NotificationDto notification);
}
