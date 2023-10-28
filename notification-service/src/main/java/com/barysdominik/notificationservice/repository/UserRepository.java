package com.barysdominik.notificationservice.repository;


import com.barysdominik.notificationservice.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUuid(String uuid);
}
