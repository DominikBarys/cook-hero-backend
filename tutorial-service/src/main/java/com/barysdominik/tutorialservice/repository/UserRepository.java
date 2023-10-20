package com.barysdominik.tutorialservice.repository;

import com.barysdominik.tutorialservice.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUuid(String uuid);
}
