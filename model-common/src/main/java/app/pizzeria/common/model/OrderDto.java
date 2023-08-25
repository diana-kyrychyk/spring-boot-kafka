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
public class OrderDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String customerName;

    private String orderDate;

    private long totalAmount;

    private String orderItem;

    private OrderStatus status;

}
