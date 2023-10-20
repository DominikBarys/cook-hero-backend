package com.barysdominik.tutorialservice.repository;

import com.barysdominik.tutorialservice.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
