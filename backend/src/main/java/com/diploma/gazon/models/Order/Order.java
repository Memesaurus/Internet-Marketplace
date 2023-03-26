package com.diploma.gazon.models.Order;

import com.diploma.gazon.models.Product.Product;
import com.diploma.gazon.models.User.User;
import com.diploma.gazon.models.User.UserAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("orders")
public class Order {
    private static final Integer CANCELLATION_PERIOD = 24 * 60 * 60;

    @Id
    private String id;
    private Instant created;
    private Instant cancellableUntil;
    private String deliveryAddress;
    private Number price;
    @DBRef
    private User user;
    @DBRef
    private Map<Product, Number> productQuantityMap;
    private OrderStatus status;

    public Order(UserAddress deliveryAddress, User user, Map<Product, Number> productQuantityMap) {
        this(deliveryAddress.toString(), user, productQuantityMap);
    }

    public Order(String deliveryAddress, User user, Map<Product, Number> productQuantityMap) {
        this.created = Instant.now();
        this.cancellableUntil = Instant.now().plusSeconds(CANCELLATION_PERIOD);
        this.deliveryAddress = deliveryAddress;
        this.user = user;
        this.productQuantityMap = productQuantityMap;
        this.price = calculatePrice();
        this.status = OrderStatus.PENDING;
    }

    private Number calculatePrice() {
        return this.productQuantityMap.entrySet().stream()
                .reduce(0.0,
                        (sum, entry) -> sum + entry.getValue().doubleValue() * entry.getKey().getPrice().doubleValue(),
                        Double::sum);
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }

    public boolean canCancel() {
        return this.cancellableUntil.isBefore(Instant.now());
    }

    public void deliver() {
        this.status = OrderStatus.DELIVERED;
    }

    public boolean isCancelled() {
        return this.status == OrderStatus.CANCELLED;
    }

    public boolean isDelivered() {
        return this.status == OrderStatus.DELIVERED;
    }

}
