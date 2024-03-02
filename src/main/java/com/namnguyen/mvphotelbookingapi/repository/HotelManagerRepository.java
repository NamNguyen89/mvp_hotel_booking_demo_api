package com.namnguyen.mvphotelbookingapi.repository;

import com.namnguyen.mvphotelbookingapi.models.entity.HotelManagerEntity;
import com.namnguyen.mvphotelbookingapi.models.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelManagerRepository extends JpaRepository<HotelManagerEntity, Long> {

    Optional<HotelManagerEntity> findByUser(UserEntity user);
}
