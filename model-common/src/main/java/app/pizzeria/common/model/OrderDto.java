package app.pizzeria.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderDto {

    private Long id;

    private String customerName;

    private String orderDate;

    private long totalAmount;

    private String orderItem;

    private OrderStatus status;

}
