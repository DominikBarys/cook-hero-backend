package com.barysdominik.auth.facade;

import com.barysdominik.auth.entity.http.AuthResponse;
import com.barysdominik.auth.entity.http.Code;
import com.barysdominik.auth.entity.user.User;
import com.barysdominik.auth.entity.user.UserRegisterDTO;
import com.barysdominik.auth.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    //write all the missing controller methods here
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        userService.register(userRegisterDTO);
        return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(HttpServletRequest httpServletRequest) {
        try {
            userService.validateToken(httpServletRequest);
            return ResponseEntity.ok(new AuthResponse(Code.PERMIT));
        } catch (IllegalArgumentException | ExpiredJwtException e) {
            return ResponseEntity.status(401).body(new AuthResponse(Code.CODE3));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse httpServletResponse) {
        return userService.login(httpServletResponse, user);
    }

}
