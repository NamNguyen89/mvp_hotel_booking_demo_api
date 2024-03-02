package com.namnguyen.mvphotelbookingapi.services;

import com.namnguyen.mvphotelbookingapi.models.entity.CustomerEntity;

import java.util.Optional;

public interface CustomerService {

    Optional<CustomerEntity> findByUserId(Long userId);
}
