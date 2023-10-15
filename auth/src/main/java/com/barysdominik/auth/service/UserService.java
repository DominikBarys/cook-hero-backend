package com.barysdominik.auth.service;

import com.barysdominik.auth.entity.user.Rank;
import com.barysdominik.auth.entity.user.Role;
import com.barysdominik.auth.entity.user.User;
import com.barysdominik.auth.entity.user.UserRegisterDTO;
import com.barysdominik.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private User saveUser(User user) {
        //for now only save(), if it will not work correctly change to saveAndFlush()
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    public void register(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setPassword(userRegisterDTO.getPassword());
        user.setEmail(userRegisterDTO.getEmail());
        user.setRole(userRegisterDTO.getRole() != null ? userRegisterDTO.getRole() : Role.USER);
        user.setRank(userRegisterDTO.getRank() != null ? userRegisterDTO.getRank() : Rank.ROOKIE);
        saveUser(user);
    }

}
