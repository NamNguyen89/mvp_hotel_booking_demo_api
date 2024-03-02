package com.namnguyen.mvphotelbookingapi.repository;

import com.namnguyen.mvphotelbookingapi.models.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    Optional<CustomerEntity> findByUserId(Long userId);

    @Query("SELECT c FROM CustomerEntity c WHERE c.user.username = :username")
    Optional<CustomerEntity> findByUsername(@Param("username") String username);

}
