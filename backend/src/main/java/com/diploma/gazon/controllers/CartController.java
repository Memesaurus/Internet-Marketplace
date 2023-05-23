package com.diploma.gazon.controllers;

import com.diploma.gazon.DTO.request.CartItemDTO;
import com.diploma.gazon.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class CartController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    public void addItemToCart(@RequestBody String id) {
        orderService.cartActionHandler(id, OrderService.CartAction.ADD);
    }

    @PostMapping("/remove")
    public void removeItemFromCart(@RequestBody String productId) {
        orderService.cartActionHandler(productId, OrderService.CartAction.REMOVE);
    }

    @PostMapping("/clear")
    public void clearCart() {
        orderService.cartActionHandler(null, OrderService.CartAction.CLEAR);
    }

    @GetMapping()
    public List<CartItemDTO> getCartItems() {
        return orderService.getCartItems();
    }
}
