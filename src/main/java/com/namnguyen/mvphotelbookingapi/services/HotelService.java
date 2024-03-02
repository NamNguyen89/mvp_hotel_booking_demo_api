package com.namnguyen.mvphotelbookingapi.services;

import com.namnguyen.mvphotelbookingapi.models.dto.HotelDTO;
import com.namnguyen.mvphotelbookingapi.models.dto.HotelRegistrationDTO;
import com.namnguyen.mvphotelbookingapi.models.entity.HotelEntity;

import java.util.List;
import java.util.Optional;

public interface HotelService {

    HotelEntity saveHotel(HotelRegistrationDTO hotelRegistrationDTO);

    HotelDTO findHotelDtoByName(String name);

    HotelDTO findHotelDtoById(Long id);

    Optional<HotelEntity> findHotelById(Long id);

    List<HotelDTO> findAllHotels();

    HotelDTO updateHotel(HotelDTO hotelDTO);

    void deleteHotelById(Long id);

    List<HotelEntity> findAllHotelsByManagerId(Long managerId);

    List<HotelDTO> findAllHotelDtosByManagerId(Long managerId);

    HotelDTO findHotelByIdAndManagerId(Long hotelId, Long managerId);

    HotelDTO updateHotelByManagerId(HotelDTO hotelDTO, Long managerId);

    void deleteHotelByIdAndManagerId(Long hotelId, Long managerId);

    HotelDTO mapHotelToHotelDto(HotelEntity hotel);

}
