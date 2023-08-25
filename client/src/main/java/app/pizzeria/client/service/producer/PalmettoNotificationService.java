package app.pizzeria.client.service.producer;

import app.pizzeria.common.model.OrderDto;

public interface PalmettoNotificationService {

    void notify(OrderDto order);
}
