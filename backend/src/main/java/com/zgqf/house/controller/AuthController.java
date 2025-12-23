package com.zgqf.house.controller;

import com.zgqf.house.entity.Buyer;
import com.zgqf.house.entity.Seller;
import com.zgqf.house.entity.User;
import com.zgqf.house.mapper.BuyerMapper;
import com.zgqf.house.mapper.SellerMapper;
import com.zgqf.house.service.AuthService;
import com.zgqf.house.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private BuyerMapper buyerMapper;

    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest, HttpSession session) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            
            User user = userService.getUserByUsername(username);
            session.setAttribute("User", user);
            
            if ("buyer".equalsIgnoreCase(user.getU_type())) {
                Buyer buyer = buyerMapper.getBuyerById(user.getU_id());
                session.setAttribute("Buyer", buyer);
                return ResponseEntity.ok(Map.of("message", "Login successful", "role", "buyer", "user", buyer));
            } else if ("seller".equalsIgnoreCase(user.getU_type())) {
                Seller seller = sellerMapper.getSellerById(user.getU_id());
                session.setAttribute("Seller", seller);
                return ResponseEntity.ok(Map.of("message", "Login successful", "role", "seller", "user", seller));
            } else if ("admin".equalsIgnoreCase(user.getU_type())) {
                session.setAttribute("Admin", user);
                return ResponseEntity.ok(Map.of("message", "Login successful", "role", "admin", "user", user));
            }
            
            return ResponseEntity.ok(Map.of("message", "Login successful", "role", "unknown", "user", user));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> registerRequest) {
        try {
            User user = new User();
            user.setU_username((String) registerRequest.get("username"));
            user.setU_password((String) registerRequest.get("password"));
            user.setU_type((String) registerRequest.get("type"));

            @SuppressWarnings("unchecked")
            Map<String, Object> additionalInfo = (Map<String, Object>) registerRequest.get("info");

            User registeredUser = authService.register(user, additionalInfo);
            return ResponseEntity.ok(Map.of("message", "Registration successful", "userId", registeredUser.getU_id()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout successful");
    }
    
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("User");
        if (user == null) {
            return ResponseEntity.status(401).body("Not logged in");
        }
        
        Object roleUser = null;
        if ("buyer".equalsIgnoreCase(user.getU_type())) {
            roleUser = session.getAttribute("Buyer");
        } else if ("seller".equalsIgnoreCase(user.getU_type())) {
            roleUser = session.getAttribute("Seller");
        } else {
            roleUser = user;
        }
        
        return ResponseEntity.ok(Map.of("user", user, "profile", roleUser != null ? roleUser : ""));
    }
}
