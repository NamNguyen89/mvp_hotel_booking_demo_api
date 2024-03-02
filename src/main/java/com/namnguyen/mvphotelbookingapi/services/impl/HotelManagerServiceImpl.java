package com.namnguyen.mvphotelbookingapi.services.impl;

import com.namnguyen.mvphotelbookingapi.models.entity.HotelManagerEntity;
import com.namnguyen.mvphotelbookingapi.models.entity.UserEntity;
import com.namnguyen.mvphotelbookingapi.repository.HotelManagerRepository;
import com.namnguyen.mvphotelbookingapi.services.HotelManagerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelManagerServiceImpl implements HotelManagerService {

    private final HotelManagerRepository hotelManagerRepository;

    @Override
    public HotelManagerEntity findByUser(UserEntity user) {
        return hotelManagerRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("HotelManager not found for user " + user.getUsername()));
    }
}
