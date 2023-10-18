package com.barysdominik.auth;

import com.barysdominik.auth.entity.user.Rank;
import com.barysdominik.auth.entity.user.Role;
import com.barysdominik.auth.entity.user.User;
import com.barysdominik.auth.repository.UserRepository;
import com.barysdominik.auth.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TestClass {

    //validate token -> odtad jest git
    //recovery mail send
    private final UserService userService;

    @PostConstruct
    public void test() {
//        User user = new User("testuser", "testuser", Role.USER, Rank.ADVANCED, LocalDate.now(),
//                4, 5, true, true);
        //userService.saveUser(user);
    }

}
