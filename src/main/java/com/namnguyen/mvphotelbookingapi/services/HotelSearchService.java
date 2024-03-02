package com.namnguyen.mvphotelbookingapi.services;

import com.namnguyen.mvphotelbookingapi.models.dto.HotelAvailabilityDTO;
import com.namnguyen.mvphotelbookingapi.models.entity.HotelEntity;

import java.time.LocalDate;
import java.util.List;

public interface HotelSearchService {

    List<HotelAvailabilityDTO> findAvailableHotelsByCityAndDate(String city, LocalDate checkinDate, LocalDate checkoutDate);

    HotelAvailabilityDTO findAvailableHotelById(Long hotelId, LocalDate checkinDate, LocalDate checkoutDate);

    HotelAvailabilityDTO mapHotelToHotelAvailabilityDto(HotelEntity hotel, LocalDate checkinDate, LocalDate checkoutDate);
}
