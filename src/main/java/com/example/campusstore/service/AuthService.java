package com.example.campusstore.service;

import com.example.campusstore.entity.Role;
import com.example.campusstore.entity.User;
import com.example.campusstore.repository.UserRepository;
import com.example.campusstore.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(String name, String email, String password) {
        if (name == null || name.isBlank() ||
            email == null || email.isBlank() ||
            password == null || password.isBlank()) {
            return "All fields are required";
        }

        if (userRepository.existsByEmail(email)) {
            return "Email already exists";
        }

        String hashedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(hashedPassword);
        user.setRole(Role.CUSTOMER);

        userRepository.save(user);
        return "SUCCESS";
    }

    public String login(String email, String password, HttpSession session) {
    Optional<User> optionalUser = userRepository.findByEmail(email);

    if (optionalUser.isEmpty()) {
        return "Invalid email or password";
    }

    User user = optionalUser.get();

    if (!passwordEncoder.matches(password, user.getPasswordHash())) {
        return "Invalid email or password";
    }

    session.setAttribute("userId", user.getId());
    session.setAttribute("role", user.getRole().name());

    return "SUCCESS";
}

    public void logout(HttpSession session) {
        session.invalidate();
    }

    public User getCurrentUser(HttpSession session) {
        Object userIdObj = session.getAttribute(SessionUtil.USER_ID);
        if (!(userIdObj instanceof Long userId)) {
            return null;
        }
        return userRepository.findById(userId).orElse(null);
    }

    public Long getCurrentUserId(HttpSession session) {
        Object userIdObj = session.getAttribute(SessionUtil.USER_ID);
        if (userIdObj instanceof Long userId) {
            return userId;
        }
        return null;
    }

    public String getCurrentUserRole(HttpSession session) {
        Object roleObj = session.getAttribute(SessionUtil.ROLE);
        if (roleObj instanceof String role) {
            return role;
        }
        return null;
    }
}