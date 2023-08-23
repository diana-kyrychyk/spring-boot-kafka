package app.pizzeria.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NotificationDto implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    private String message;

    private OrderDto order;

}
