package com.diploma.gazon.services;

import com.diploma.gazon.DTO.request.CartDTO;
import com.diploma.gazon.DTO.response.OrderResponseDTO;
import com.diploma.gazon.exceptions.OrderException;
import com.diploma.gazon.exceptions.NotFoundException;
import com.diploma.gazon.exceptions.UnauthorizedException;
import com.diploma.gazon.mappers.OrderMapper;
import com.diploma.gazon.mappers.ProductMapper;
import com.diploma.gazon.models.Order.Order;
import com.diploma.gazon.models.Order.ProductOrder;
import com.diploma.gazon.models.Product.Product;
import com.diploma.gazon.models.User.User;
import com.diploma.gazon.repositories.OrderRepository;
import com.diploma.gazon.services.ProductServices.ProductService;
import com.diploma.gazon.services.UserServices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ProductMapper productMapper;


    public OrderResponseDTO getOrderById(String orderId) {
        User currentUser = userService.getCurrentUser();

        if (isUserNotAuthorized(currentUser)) {
            throw new UnauthorizedException();
        }

        Order order = getOrElseThrow(orderId);

        return orderMapper.toOrderResponseDto(order);
    }


    public OrderResponseDTO placeOrder(CartDTO cartDTO) {
        User currentUser = userService.getCurrentUser();
        Number price = calculateTotalPrice(cartDTO);

        Set<ProductOrder> productOrders = getProductsFromCart(cartDTO);

        Order order = new Order(cartDTO.getAddress(), currentUser, productOrders, price);

        orderRepository.save(order);
        return orderMapper.toOrderResponseDto(order);
    }

    private Set<ProductOrder> getProductsFromCart(CartDTO cartDTO) {
        Set<ProductOrder> productOrders = new HashSet<>();

        cartDTO.getProducts().forEach((productId, quantity) -> {
            Product product = productService.getOrElseThrow(productId);
            ProductOrder productOrder = new ProductOrder(product, quantity);
            productOrders.add(productOrder);
        });

        return productOrders;
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

    public List<OrderResponseDTO> getAllOrdersOfCurrentUser() {
        User currentUser = userService.getCurrentUser();

        if (isUserNotAuthorized(currentUser)) {
            throw new UnauthorizedException();
        }

        List<Order> orders = orderRepository.findByUserId(currentUser.getId());
        return orderMapper.toOrderResponseDto(orders);
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
