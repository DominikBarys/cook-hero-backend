package com.barysdominik.auth.repository;

import com.barysdominik.auth.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
