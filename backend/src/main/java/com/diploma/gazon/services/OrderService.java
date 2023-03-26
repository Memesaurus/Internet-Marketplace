package com.diploma.gazon.services;

import com.diploma.gazon.DTO.request.CartDTO;
import com.diploma.gazon.exceptions.OrderException;
import com.diploma.gazon.exceptions.NotFoundException;
import com.diploma.gazon.exceptions.UnauthorizedException;
import com.diploma.gazon.models.Order.Order;
import com.diploma.gazon.models.Product.Product;
import com.diploma.gazon.models.User.User;
import com.diploma.gazon.repositories.OrderRepository;
import com.diploma.gazon.services.ProductServices.ProductService;
import com.diploma.gazon.services.UserServices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;


    public Order getOrderById(String orderId) {
        User currentUser = userService.getCurrentUser();

        if (isUserNotAuthorized(currentUser)) {
            throw new UnauthorizedException();
        }

        return getOrElseThrow(orderId);
    }

    public List<Order> getAllOrdersOfUser() {
        User currentUser = userService.getCurrentUser();

        if (isUserNotAuthorized(currentUser)) {
            throw new UnauthorizedException();
        }

        return orderRepository.findByUserId(currentUser.getId());
    }

    public Order placeOrder(CartDTO cartDTO) {
        User currentUser = userService.getCurrentUser();

        Number price = calculateTotalPrice(cartDTO);

        Order order = new Order(cartDTO.getAddress(), currentUser, cartDTO.getProducts(), price);

        orderRepository.save(order);
        return order;
    }

    private Number calculateTotalPrice(CartDTO cartDTO) {
        return cartDTO.getProducts().entrySet().stream()
                .reduce(0.0, (total, entry) ->
                        {
                            Product product = productService.getOrElseThrow(entry.getKey());
                            return total + product.getPrice().doubleValue() * entry.getValue().doubleValue();
                        },
                        Double::sum);
    }

    public void cancelOrder(String orderId) {
        Order order = getOrElseThrow(orderId);

        if (isUserNotAuthorized(order.getUser())) {
            throw new UnauthorizedException();
        }

        if (!order.canCancel() && !order.isDelivered()) {
            throw new OrderException("Заказ не может быть отменен");
        }

        order.cancel();

        orderRepository.save(order);
    }

    private boolean isUserNotAuthorized(User user) {
        User currentUser = userService.getCurrentUser();

        return !currentUser.equals(user) && !currentUser.isAdmin();
    }

    public void deliverOrder(String orderId) {
        Order order = getOrElseThrow(orderId);

        if (order.isCancelled()) {
            throw new OrderException("Заказ отменен и не может быть доставлен");
        }
        order.deliver();

        orderRepository.save(order);
    }

    private Order getOrElseThrow(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(NotFoundException::new);
    }
}
