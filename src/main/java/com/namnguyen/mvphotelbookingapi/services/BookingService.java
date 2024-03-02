package com.namnguyen.mvphotelbookingapi.services;

import com.namnguyen.mvphotelbookingapi.models.dto.BookingDTO;
import com.namnguyen.mvphotelbookingapi.models.dto.BookingInitiationDTO;
import com.namnguyen.mvphotelbookingapi.models.entity.BookingEntity;

import java.util.List;

public interface BookingService {

    BookingEntity saveBooking(BookingInitiationDTO bookingInitiationDTO, Long customerId);

    BookingDTO confirmBooking(BookingInitiationDTO bookingInitiationDTO, Long customerId);

//    List<BookingDTO> findAllBookings();
//
//    BookingDTO findBookingById(Long bookingId);

    List<BookingDTO> findBookingsByCustomerId(Long customerId);

    BookingDTO findBookingByIdAndCustomerId(Long bookingId, Long customerId);

    List<BookingDTO> findBookingsByManagerId(Long managerId);

    BookingDTO findBookingByIdAndManagerId(Long bookingId, Long managerId);

    BookingDTO mapBookingModelToBookingDto(BookingEntity booking);

}
