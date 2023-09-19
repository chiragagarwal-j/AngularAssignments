package com.spring.learningRest.controller;

import com.spring.learningRest.entity.Cycle;
import com.spring.learningRest.entity.User;
import com.spring.learningRest.repository.CycleRepository;
import com.spring.learningRest.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/cycleStocks")
public class CycleRestController {

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Cycle> getAllCycleStocks() {
        return (List<Cycle>) cycleRepository.findAll();
    }

    @PostMapping("/addCycle")
    public ResponseEntity<?> addCycle(@RequestBody Cycle cycle) {
        System.out.println(cycle);
        try {
            if (cycle == null) {
                return ResponseEntity.badRequest().body("Cycle information is missing");
            }
            cycleRepository.save(cycle);
            List<Cycle> allcycles = (List<Cycle>) cycleRepository.findAll();

            return ResponseEntity.ok(allcycles);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cycle addition failed: " + e.getMessage());
        }
    }

    // @PostMapping("/{id}/return")
    // public ResponseEntity<?> returnCycle(@PathVariable int id) {
    // Optional<Cycle> optionalCycle = cycleStockRepo.findById(id);
    // if (optionalCycle.isPresent()) {
    // Cycle cycle = optionalCycle.get();
    // int currentAvailableCycles = cycle.getAvailableCycles();
    // cycle.setAvailableCycles(currentAvailableCycles + 1);
    // cycleStockRepo.save(cycle);
    // List<Cycle> allCycles = (List<Cycle>) cycleStockRepo.findAll();
    // return ResponseEntity.ok(allCycles);
    // } else {

    // return ResponseEntity.notFound().build();
    // }
    // }

    // @PostMapping("/restock")
    // public ResponseEntity<?> restockCycle(@RequestBody Map<String, Integer>
    // requestData) {
    // int brandId = requestData.get("brandId");
    // int restockQuantity = requestData.get("restockQuantity");
    // System.out.println(brandId + restockQuantity);

    // Optional<Cycle> optionalCycle = cycleStockRepo.findById(brandId);

    // if (optionalCycle.isPresent()) {
    // Cycle cycle = optionalCycle.get();
    // int currentAvailableCycles = cycle.getAvailableCycles();
    // int newAvailableCycles = currentAvailableCycles + restockQuantity;
    // cycle.setAvailableCycles(newAvailableCycles);
    // cycleStockRepo.save(cycle);
    // List<Cycle> allCycles = (List<Cycle>) cycleStockRepo.findAll();
    // return ResponseEntity.ok(allCycles);
    // } else {

    // return ResponseEntity.notFound().build();
    // }
    // }

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
