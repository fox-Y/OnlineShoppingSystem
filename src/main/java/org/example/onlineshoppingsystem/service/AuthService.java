package org.example.onlineshoppingsystem.service;

import lombok.RequiredArgsConstructor;
import org.example.onlineshoppingsystem.auth.JwtUtil;
import org.example.onlineshoppingsystem.common.dto.LoginReq;
import org.example.onlineshoppingsystem.common.dto.SignupReq;
import org.example.onlineshoppingsystem.dao.UserRepository;
import org.example.onlineshoppingsystem.domain.entity.User;
import org.example.onlineshoppingsystem.domain.enums.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    @Transactional
    public void signup(SignupReq req) {
        if (userRepo.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username exists");
        }
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email exists");
        }
        User u = new User();
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setRole(Role.USER);
        userRepo.save(u);
    }

    @Transactional
    public void signupAdmin(SignupReq req) {
        if (userRepo.existsByUsername(req.getUsername())) throw new IllegalArgumentException("Username exists");
        if (userRepo.existsByEmail(req.getEmail()))       throw new IllegalArgumentException("Email exists");
        User u = new User();
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setRole(Role.ADMIN);
        userRepo.save(u);
    }

    public String login(LoginReq req) {
        var u = userRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Incorrect credentials"));
        if (!encoder.matches(req.getPassword(), u.getPassword())) {
            throw new IllegalArgumentException("Incorrect credentials");
        }
        return jwt.generateToken(u.getUserId(), u.getUsername(), u.getRole().name());
    }
}