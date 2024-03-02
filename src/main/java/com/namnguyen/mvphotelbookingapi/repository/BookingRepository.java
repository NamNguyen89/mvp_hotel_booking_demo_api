package com.namnguyen.mvphotelbookingapi.repository;

import com.namnguyen.mvphotelbookingapi.models.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    List<BookingEntity> findBookingsByCustomerId(Long customerId);

    Optional<BookingEntity> findBookingByIdAndCustomerId(Long bookingId, Long customerId);

    List<BookingEntity> findBookingsByHotelId(Long hotelId);

    Optional<BookingEntity> findBookingByIdAndHotel_HotelManagerId(Long bookingId, Long hotelManagerId);

}
