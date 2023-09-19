package com.spring.learningRest.controller;

import com.spring.learningRest.entity.CycleStock;
import com.spring.learningRest.entity.User;
import com.spring.learningRest.repository.CycleStockRepository;
import com.spring.learningRest.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/cycleStocks")
public class CycleRestController {

    @Autowired
    private CycleStockRepository cycleStockRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<CycleStock> getAllCycleStocks() {
        return (List<CycleStock>) cycleStockRepo.findAll();
    }

    @PostMapping("/{id}/borrow")
    public ResponseEntity<?> borrowCycle(@PathVariable int id) {
        Optional<CycleStock> optionalCycle = cycleStockRepo.findById(id);
        if (optionalCycle.isPresent()) {
            CycleStock cycle = optionalCycle.get();
            int currentAvailableCycles = cycle.getAvailableCycles();
            if (currentAvailableCycles > 0) {
                cycle.setAvailableCycles(currentAvailableCycles - 1);
                cycleStockRepo.save(cycle);
                List<CycleStock> allCycles = (List<CycleStock>) cycleStockRepo.findAll();
                return ResponseEntity.ok(allCycles);
            } else {
                return ResponseEntity.badRequest().body("No available cycles to borrow");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<?> returnCycle(@PathVariable int id) {
        Optional<CycleStock> optionalCycle = cycleStockRepo.findById(id);
        if (optionalCycle.isPresent()) {
            CycleStock cycle = optionalCycle.get();
            int currentAvailableCycles = cycle.getAvailableCycles();
            cycle.setAvailableCycles(currentAvailableCycles + 1);
            cycleStockRepo.save(cycle);
            List<CycleStock> allCycles = (List<CycleStock>) cycleStockRepo.findAll();
            return ResponseEntity.ok(allCycles);
        } else {

            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/restock")
    public ResponseEntity<?> restockCycle(@RequestBody Map<String, Integer> requestData) {
        int brandId = requestData.get("brandId");
        int restockQuantity = requestData.get("restockQuantity");
        System.out.println(brandId + restockQuantity);

        Optional<CycleStock> optionalCycle = cycleStockRepo.findById(brandId);

        if (optionalCycle.isPresent()) {
            CycleStock cycle = optionalCycle.get();
            int currentAvailableCycles = cycle.getAvailableCycles();
            int newAvailableCycles = currentAvailableCycles + restockQuantity;
            cycle.setAvailableCycles(newAvailableCycles);
            cycleStockRepo.save(cycle);
            List<CycleStock> allCycles = (List<CycleStock>) cycleStockRepo.findAll();
            return ResponseEntity.ok(allCycles);
        } else {

            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/registration")
    public String registrationForm(Model model) {
        return "userRegistration";
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            if (userRepository.existsByName(user.getName())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }

    @GetMapping("/login")
    public String LoginForm(Model model) {
        return "userLogin";
    }

    @PostMapping("/login")
    public String LoginonSubmit(@RequestParam String username, @RequestParam String password, Model model) {
        Optional<User> user = userRepository.findByName(username);
        if (user != null && userMatchesPassword(user.get(), password)) {
            return "redirect:/restock";
        } else {
            model.addAttribute("error", "Invalid Crudentials");
            return "userLogin";
        }
    }

    private boolean userMatchesPassword(User user, String password) {
        return user.getPassword().equals(password);
    }
}
