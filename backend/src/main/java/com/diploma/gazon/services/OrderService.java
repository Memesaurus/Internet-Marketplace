package com.diploma.gazon.services;

import com.diploma.gazon.DTO.request.CartItemDTO;
import com.diploma.gazon.DTO.response.OrderResponseDTO;
import com.diploma.gazon.exceptions.NotFoundException;
import com.diploma.gazon.exceptions.OrderException;
import com.diploma.gazon.exceptions.UnauthorizedException;
import com.diploma.gazon.mappers.CartMapper;
import com.diploma.gazon.mappers.OrderMapper;
import com.diploma.gazon.mappers.ProductMapper;
import com.diploma.gazon.models.CartItem;
import com.diploma.gazon.models.Member;
import com.diploma.gazon.models.Order.Order;
import com.diploma.gazon.models.Product.Product;
import com.diploma.gazon.models.User.User;
import com.diploma.gazon.repositories.OrderRepository;
import com.diploma.gazon.repositories.UserRepository;
import com.diploma.gazon.services.ProductServices.ProductService;
import com.diploma.gazon.services.UserServices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CartMapper cartMapper;

    public OrderResponseDTO getOrderById(String orderId) {
        User currentUser = userService.getCurrentUser();

        if (isUserNotAuthorized(currentUser)) {
            throw new UnauthorizedException();
        }

        Order order = getOrElseThrow(orderId);

        return orderMapper.toOrderResponseDto(order);
    }

    public void placeOrder() {
        User currentUser = userService.getCurrentUser();

        if (!(currentUser instanceof Member currentMember)) {
            throw new UnauthorizedException();
        }

        List<CartItem> items = currentMember.getCart();
        Number price = calculateTotalPrice(items);

        Order order = new Order(
                currentMember,
                items,
                price
        );

        orderRepository.save(order);

        items.clear();
        userRepository.save(currentMember);
    }

    private Number calculateTotalPrice(List<CartItem> items) {
        return items.stream()
                .reduce(0.0, (total, item) ->
                                total + item.getProduct().getPrice().doubleValue() * item.getQuantity().doubleValue(),
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

    public void cartActionHandler(String productId, CartAction action) {
        Product product = productService.getOrElseThrow(productId);
        User user = userService.getCurrentUser();

        if (!(user instanceof Member memberUser)) {
            throw new UnauthorizedException();
        }

        List<CartItem> memberCart = memberUser.getCart();

        // Probably a better way to do this but honestly running out of time and CBA
        if (action == CartAction.ADD) {
            addItemToCart(memberCart, product);
        } else if (action == CartAction.REMOVE) {
            removeItemFromCart(memberCart, product);
        } else {
            memberCart.clear();
        }

        userRepository.save(memberUser);
    }

    private void addItemToCart(List<CartItem> cart, Product product) {
        cartContainsItem(cart, product).findFirst()
                .ifPresentOrElse(cartItem -> cartItem.setQuantity(cartItem.getQuantity() + 1),
                        () -> cart.add(new CartItem(product, 1)));
    }

    private void removeItemFromCart(List<CartItem> cart, Product product) {
        cartContainsItem(cart, product).findFirst()
                .ifPresent(cartItem -> {
                    if (cartItem.getQuantity() == 1) {
                        cart.remove(cartItem);
                    } else {
                        cartItem.setQuantity(cartItem.getQuantity() - 1);
                    }
                });
    }

    private Stream<CartItem> cartContainsItem(List<CartItem> cart, Product product) {
        return cart.stream()
                .filter(p -> Objects.equals(p.getProduct().getId(), product.getId()));
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

    public List<CartItemDTO> getCartItems() {
        User user = userService.getCurrentUser();

        if (!(user instanceof Member memberUser)) {
            throw new UnauthorizedException();
        }

        List<CartItem> userCart = memberUser.getCart();

        return cartMapper.toCartItemDTOList(userCart);
    }

    public enum CartAction {
        ADD,
        REMOVE,
        CLEAR
    }
}
