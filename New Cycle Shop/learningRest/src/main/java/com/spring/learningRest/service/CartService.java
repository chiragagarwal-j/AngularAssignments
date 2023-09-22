package com.spring.learningRest.service;

import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.spring.learningRest.business.CartDTO;
import com.spring.learningRest.entity.Cart;
import com.spring.learningRest.entity.CartItem;
import com.spring.learningRest.entity.Cycle;
import com.spring.learningRest.entity.Order;
import com.spring.learningRest.entity.User;
import com.spring.learningRest.repository.CartItemRepository;
import com.spring.learningRest.repository.CartRepository;
import com.spring.learningRest.repository.CycleRepository;
import com.spring.learningRest.repository.OrderRepository;
import com.spring.learningRest.repository.UserRepository;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    public CartDTO getCart() {
        return createCartDTO();
    }

    private CartDTO createCartDTO() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByName(authentication.getName()).get();
        Cart cart = cartRepository.findByUser(user).orElseGet(Cart::new);

        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setCartItems(cart.getCartItems());
        cartDTO.setTotalPrice(cart.getCartItems().stream().mapToInt(CartItem::getTotalPrice).sum());
        cartDTO.setTotalQuantity(cart.getCartItems().stream().mapToInt(CartItem::getQuantity).sum());

        return cartDTO;
    }

    public void save(Cart cart) {
        cartRepository.save(cart);
    }

    public Cart addToCart(int id, int quantity) {
        Optional<Cycle> existingCycle = cycleRepository.findById(id);

        Cycle cycle = existingCycle.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByName(authentication.getName()).get();
        Optional<Cart> existingCart = cartRepository.findByUser(user);

        if (existingCart.isEmpty()) {
            Cart newCart = new Cart();
            newCart.setUser(user);
            cartRepository.save(newCart);
            existingCart = Optional.of(newCart);
        }
        Cart cart = existingCart.get();
        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getCycle().getId() == cycle.getId())
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            int maxQuantity = cartItem.getQuantity() + quantity > cycle.getStock() ? cycle.getStock()
                    : quantity + cartItem.getQuantity();
            cartItem.setQuantity(maxQuantity);
            cartItem.setTotalPrice(cycle.getPrice() * cartItem.getQuantity());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCycle(cycle);
            cartItem.setQuantity(quantity);
            cartItem.setTotalPrice(cycle.getPrice() * quantity);
            cart.getCartItems().add(cartItem);
        }
        return cart;
    }

    public Cart removeFromCart(int id, int quantity) {
        Optional<Cycle> existingCycle = cycleRepository.findById(id);
        if (existingCycle.isEmpty()) {
            return null;
        } else if (quantity <= 0) {
            return null;
        }
        Cycle cycle = existingCycle.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByName(authentication.getName()).get();

        Cart cart = cartRepository.findByUser(user).orElseGet(Cart::new);

        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getCycle().getId() == cycle.getId())
                .findFirst();

        CartItem cartItem = existingCartItem.get();

        cartItem.setQuantity(cartItem.getQuantity() - quantity);
        cartItem.setTotalPrice(cycle.getPrice() * cartItem.getQuantity());

        if (cartItem.getQuantity() == 0) {
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        }
        return cart;
    }

    public Cart checkout(int cartItemId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByName(authentication.getName()).get();
        Cart cart = cartRepository.findByUser(user).orElseGet(Cart::new);
        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getId() == cartItemId)
                .findFirst();

        // add in it in order
        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            Cycle cycle = cartItem.getCycle();
            cycle.setStock(cycle.getStock() - cartItem.getQuantity());
            cycleRepository.save(cycle);
            Optional<Order> existingOrder = orderRepository.findByUserAndProductOptional(user, cycle);
            if (existingOrder.isEmpty()) {
                Order order = new Order();
                int quantity = cartItem.getQuantity();
                order.setUser(user);
                order.setCycle(cycle);
                order.setQuantity(quantity);
                orderRepository.save(order);

                cart.getCartItems().remove(cartItem);
                cartItemRepository.delete(cartItem);
            }
        }
        return cart;
    }

    public Cart checkoutAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByName(authentication.getName()).get();
        Cart cart = cartRepository.findByUser(user).orElseGet(Cart::new);

        for (CartItem cartItem : cart.getCartItems()) {
            Cycle cycle = cartItem.getCycle();
            cycle.setStock(cycle.getStock() - cartItem.getQuantity());
            cycleRepository.save(cycle);
            Optional<Order> existingOrder = orderRepository.findByUserAndProductOptional(user, cycle);
            if (existingOrder.isEmpty()) {
                Order order = new Order();
                int quantity = cartItem.getQuantity();
                order.setUser(user);
                order.setCycle(cycle);
                order.setQuantity(quantity);
                orderRepository.save(order);

                cart.getCartItems().remove(cartItem);
                cartItemRepository.delete(cartItem);
            }
        }

        return cart;

    }
}
