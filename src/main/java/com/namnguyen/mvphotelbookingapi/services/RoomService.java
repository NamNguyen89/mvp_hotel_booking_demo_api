package com.namnguyen.mvphotelbookingapi.services;


import com.namnguyen.mvphotelbookingapi.models.dto.RoomDTO;
import com.namnguyen.mvphotelbookingapi.models.entity.HotelEntity;
import com.namnguyen.mvphotelbookingapi.models.entity.RoomEntity;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    RoomEntity saveRoom(RoomDTO roomDTO, HotelEntity hotel);

    List<RoomEntity> saveRooms(List<RoomDTO> roomDTOs, HotelEntity hotel);

    Optional<RoomEntity> findRoomById(Long id);


    RoomEntity updateRoom(RoomDTO roomDTO);

    void deleteRoom(Long id);

    RoomEntity mapRoomDtoToRoom(RoomDTO roomDTO, HotelEntity hotel);

    RoomDTO mapRoomToRoomDto(RoomEntity room);

}
