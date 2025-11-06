package org.example.onlineshoppingsystem.web;

import jakarta.validation.Valid;
import org.example.onlineshoppingsystem.common.dto.LoginReq;
import org.example.onlineshoppingsystem.common.dto.SignupReq;
import org.example.onlineshoppingsystem.common.dto.TokenRes;
import org.example.onlineshoppingsystem.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService auth;
    public AuthController(AuthService auth) { this.auth = auth; }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupReq req) {
        auth.signup(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public TokenRes login(@RequestBody LoginReq req) {
        return new TokenRes(auth.login(req));
    }
}
