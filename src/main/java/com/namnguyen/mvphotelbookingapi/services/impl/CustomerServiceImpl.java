package com.namnguyen.mvphotelbookingapi.services.impl;

import com.namnguyen.mvphotelbookingapi.models.entity.CustomerEntity;
import com.namnguyen.mvphotelbookingapi.repository.CustomerRepository;
import com.namnguyen.mvphotelbookingapi.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Optional<CustomerEntity> findByUserId(Long userId) {
        return customerRepository.findByUserId(userId);
    }


}
