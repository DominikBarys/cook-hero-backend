package com.barysdominik.tutorialservice.service;

import com.barysdominik.tutorialservice.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
}
