package com.spring.learningRest.controller;

import com.spring.learningRest.business.CartDTO;
import com.spring.learningRest.entity.Cart;
import com.spring.learningRest.entity.CartItem;
import com.spring.learningRest.entity.Cycle;
import com.spring.learningRest.entity.User;
import com.spring.learningRest.repository.CartItemRepository;
import com.spring.learningRest.repository.CartRepository;
// import com.spring.learningRest.entity.User;
import com.spring.learningRest.repository.CycleRepository;
// import com.spring.learningRest.repository.UserRepository;
// import com.spring.learningRest.service.UserService;
import com.spring.learningRest.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cycles")
public class CycleRestController {

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @GetMapping("/list")
    public List<Cycle> getAllCycleStocks() {
        return (List<Cycle>) cycleRepository.findAll();
    }

    @PostMapping("/{id}/borrow")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    List<Cycle> borrowCycle(@PathVariable("id") int id) {
        Optional<Cycle> cycle = cycleRepository.findById(id);
        if (cycle.isPresent()) {
            Cycle c = cycle.get();
            if (c.getNumAvailable() > 0) {
                c.setNumBorrowed(c.getNumBorrowed() + 1);
                cycleRepository.save(c);
            }
            return (List<Cycle>) cycleRepository.findAll();
        }
        return null;
    }

    @PostMapping("/{id}/return")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    List<Cycle> returnCycle(@PathVariable("id") int id) {
        Optional<Cycle> cycle = cycleRepository.findById(id);
        if (cycle.isPresent()) {
            Cycle c = cycle.get();
            if (c.getNumAvailable() > 0) {
                c.setNumBorrowed(c.getNumBorrowed() - 1);
                cycleRepository.save(c);
            }
            return (List<Cycle>) cycleRepository.findAll();
        }
        return null;
    }

    @PostMapping("/{id}/restock")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    List<Cycle> restockCycle(@PathVariable("id") int id, @RequestParam("quantity") int quantity) {
        Optional<Cycle> cycle = cycleRepository.findById(id);
        if (cycle.isPresent()) {
            Cycle c = cycle.get();
            c.setStock(c.getStock() + quantity);
            return (List<Cycle>) cycleRepository.findAll();
        } else {
            return null;
        }
    }

    @PostMapping("cart/{id}/add")
    @ResponseBody
    @Transactional
    ResponseEntity<String> addToCart(@PathVariable("id") int id, @RequestParam("quantity") int quantity) {
        Optional<Cycle> existingCycle = cycleRepository.findById(id);
        if (existingCycle.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cycle not found");
        }
        if (quantity <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity must be positive");
        }
        Cycle cycle = existingCycle.get();
        if (cycle.getNumAvailable() < quantity) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not enough cycles available");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByName(authentication.getName()).get();

        Cart cart = cartRepository.findByUser(user).orElseGet(Cart::new);

        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getCycle().getId() == cycle.getId())
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setTotalPrice(cycle.getPrice() * cartItem.getQuantity());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCycle(cycle);
            cartItem.setQuantity(quantity);
            cartItem.setTotalPrice(cycle.getPrice() * quantity);
            cart.getCartItems().add(cartItem);
        }
        cartRepository.save(cart);
        return ResponseEntity.ok("Cycle added to cart");
    }

    @PostMapping("cart/{id}/remove")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @Transactional
    ResponseEntity<String> removeFromCart(@PathVariable("id") int id, @RequestParam("quantity") int quantity) {
        Optional<Cycle> existingCycle = cycleRepository.findById(id);
        if (existingCycle.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cycle not found");
        }
        if (quantity <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity must be positive");
        }
        Cycle cycle = existingCycle.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByName(authentication.getName()).get();

        Cart cart = cartRepository.findByUser(user).orElseGet(Cart::new);

        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getCycle().getId() == cycle.getId())
                .findFirst();

        if (existingCartItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cycle not in cart");
        }

        CartItem cartItem = existingCartItem.get();
        if (cartItem.getQuantity() < quantity) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not enough cycles in cart");
        }

        cartItem.setQuantity(cartItem.getQuantity() - quantity);
        cartItem.setTotalPrice(cycle.getPrice() * cartItem.getQuantity());

        if (cartItem.getQuantity() == 0) {
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        }

        cartRepository.save(cart);
        return ResponseEntity.ok("Cycle removed from cart");
    }

    @GetMapping("cart")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @Transactional
    ResponseEntity<CartDTO> getCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByName(authentication.getName()).get();

        Cart cart = cartRepository.findByUser(user).orElseGet(Cart::new);

        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setCartItems(cart.getCartItems());
        cartDTO.setTotalPrice(cart.getCartItems().stream().mapToInt(CartItem::getTotalPrice).sum());
        cartDTO.setTotalQuantity(cart.getCartItems().stream().mapToInt(CartItem::getQuantity).sum());
        return ResponseEntity.ok(cartDTO);
    }

    @PostMapping("cart/checkout/{id}")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    @Transactional
    ResponseEntity<String> checkout(@PathVariable("id") int cartItemId) {

        //TODO: single cartitem checkout, remove the cartitem from cart and add to order then decrease the stock of cycle
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByName(authentication.getName()).get();

        return ResponseEntity.ok("Checkout successful");
    }


}
