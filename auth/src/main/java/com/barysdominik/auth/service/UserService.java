package com.barysdominik.auth.service;

import com.barysdominik.auth.entity.http.AuthResponse;
import com.barysdominik.auth.entity.http.Code;
import com.barysdominik.auth.entity.user.Rank;
import com.barysdominik.auth.entity.user.Role;
import com.barysdominik.auth.entity.user.User;
import com.barysdominik.auth.entity.user.UserRegisterDTO;
import com.barysdominik.auth.exception.DuplicateMailException;
import com.barysdominik.auth.exception.DuplicateUsernameException;
import com.barysdominik.auth.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CookieService cookieService;
    @Value("${jwt.exp}")
    private int exp;
    @Value("${jwt.refresh}")
    private int refreshExp;

    private User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    public String generateToken(String username, int exp) {
        return jwtService.generateToken(username, exp);
    }

    public void validateToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ExpiredJwtException, IllegalArgumentException {
        String token = null;
        String refresh = null;
        for(Cookie cookie : Arrays.stream(request.getCookies()).toList()) {
            if(cookie.getName().equals("token")) {
                token = cookie.getValue();
            } else if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }
        try {
            jwtService.validateToken(token);
        } catch (ExpiredJwtException | IllegalArgumentException e) {
            jwtService.validateToken(refresh);
            //to rzuca nulla jak zwykly token jest nullem, nie mozna go tez odnowic bo nie ma tutaj nazwy uzytkownika
            //nieeeleganckim rozwiazaniem byloby zdobycie subjecta z refresha
            Cookie authorizationToken = cookieService.generateCookie(
                    "token",
                    jwtService.refreshToken(refresh, exp),
                    exp
            );
            Cookie refreshToken = cookieService.generateCookie(
                    "refresh",
                    jwtService.refreshToken(refresh, refreshExp),
                    refreshExp
            );
            response.addCookie(authorizationToken);
            response.addCookie(refreshToken);
        }
    }

    public void register(UserRegisterDTO userRegisterDTO) throws DuplicateUsernameException, DuplicateMailException {
        userRepository.findUserByUsername(userRegisterDTO.getUsername()).ifPresent(value -> {
            throw new DuplicateUsernameException("Użytkownik o takiej nazwie już istnieje");
        });
        userRepository.findUserByEmail(userRegisterDTO.getEmail()).ifPresent(value -> {
            throw new DuplicateMailException("Użytkownik o takim mailu już istnieje");
        });
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setPassword(userRegisterDTO.getPassword());
        user.setEmail(userRegisterDTO.getEmail());
        user.setRole(Role.USER);
        user.setRank(Rank.ROOKIE);
        saveUser(user);
    }

    public ResponseEntity<?> login(HttpServletResponse response, User authRequest) {
        User user = userRepository.findUserByUsername(authRequest.getUsername()).orElse(null);
        if (user != null) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            if(authentication.isAuthenticated()) {
                Cookie refresh = cookieService.generateCookie(
                        "refresh",
                        generateToken(authRequest.getUsername(), refreshExp),
                        refreshExp
                );
                Cookie token = cookieService.generateCookie(
                        "token",
                        generateToken(authRequest.getUsername(), exp),
                        exp);
                response.addCookie(refresh);
                response.addCookie(token);
                return ResponseEntity.ok(
                        UserRegisterDTO
                                .builder()
                                .username(user.getUsername())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .rank(user.getRank())
                                .build()
                );
            } else {
                return ResponseEntity.ok(new AuthResponse(Code.LOGIN_FAILED));
            }
        }
        return ResponseEntity.ok(new AuthResponse(Code.USER_NOT_FOUND));
    }

    public void promoteUserToAdmin(UserRegisterDTO userRegisterDTO) {
        userRepository.findUserByUsername(userRegisterDTO.getUsername()).ifPresent(value -> {
            value.setRole(Role.ADMIN);
            userRepository.save(value);
        });
    }
}