package com.barysdominik.auth;

import com.barysdominik.auth.entity.user.Role;
import com.barysdominik.auth.entity.user.User;
import com.barysdominik.auth.repository.UserRepository;
import com.barysdominik.auth.service.EmailService;
import com.barysdominik.auth.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestClass {

//    private final UserRepository userRepository;
//
//    @PostConstruct
//    public void promote() {
//        User userToChangeRole = userRepository.findUserByUuid("cc1728e7-b516-4c6b-b335-4e1c5d79b9ff").orElse(null);
//        if(userToChangeRole != null) {
//            List<Role> roles = Arrays.stream(Role.values()).toList();
//
//            if(roles.stream().anyMatch(name -> name.toString().equals("ADMIN"))) {
//                userToChangeRole.setRole(Role.valueOf("ADMIN"));
//                userRepository.save(userToChangeRole);
//            }
//        }
//    }

}
