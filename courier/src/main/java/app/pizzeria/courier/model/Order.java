package app.pizzeria.courier.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

    private Long id;

    private String customerName;

    private String orderDate;

    private long totalAmount;

    private String orderItem;

    public Order(String orderJson) {
        // Use Jackson ObjectMapper to deserialize the JSON string
        ObjectMapper mapper = new ObjectMapper();
        try {
            Order order = mapper.readValue(orderJson, Order.class);
            this.id = order.getId();
            this.customerName = order.getCustomerName();
            this.orderDate = order.getOrderDate();
            this.totalAmount = order.getTotalAmount();
            this.orderItem = order.getOrderItem();
        } catch (JsonProcessingException e) {
            log.error("Failed to map order");
        }
    }

}
