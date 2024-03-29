package com.namnguyen.mvphotelbookingapi.repository;

import com.namnguyen.mvphotelbookingapi.models.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
}
