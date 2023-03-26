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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        if (!isUserAuthorized(currentUser)) {
            throw new UnauthorizedException();
        }

        return getOrElseThrow(orderId);
    }

    public List<Order> getAllOrdersOfUser() {
        User currentUser = userService.getCurrentUser();

        if (!isUserAuthorized(currentUser)) {
            throw new UnauthorizedException();
        }

        return orderRepository.findByUserId(currentUser.getId());
    }

    public Order placeOrder(CartDTO cartDTO) {
        User currentUser = userService.getCurrentUser();

        Map<Product, Number> productMap = new HashMap<>();

        cartDTO.getProductIdsQuantity()
                .forEach((s, integer) -> productMap.put(productService.getOrElseThrow(s), integer));

        Order order = new Order(cartDTO.getAddress(), currentUser, productMap);

        orderRepository.save(order);
        return order;
    }

    public void cancelOrder(String orderId) {
        Order order = getOrElseThrow(orderId);

        if (!isUserAuthorized(order.getUser())) {
            throw new UnauthorizedException();
        }

        if (!order.canCancel() && !order.isDelivered()) {
            throw new OrderException("Заказ не может быть отменен");
        }

        order.cancel();

        orderRepository.save(order);
    }

    private boolean isUserAuthorized(User user) {
        User currentUser = userService.getCurrentUser();

        return currentUser.equals(user) || currentUser.isAdmin();
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
