package app.pizzeria.palmetto.service.producer;

import app.pizzeria.common.model.NotificationDto;

public interface CourierNotificationService {

    void notify(NotificationDto notification);
}
