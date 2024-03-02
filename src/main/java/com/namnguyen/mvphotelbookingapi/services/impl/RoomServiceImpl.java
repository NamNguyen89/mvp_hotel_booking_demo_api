package com.namnguyen.mvphotelbookingapi.services.impl;

import com.namnguyen.mvphotelbookingapi.models.dto.RoomDTO;
import com.namnguyen.mvphotelbookingapi.models.entity.HotelEntity;
import com.namnguyen.mvphotelbookingapi.models.entity.RoomEntity;
import com.namnguyen.mvphotelbookingapi.repository.RoomRepository;
import com.namnguyen.mvphotelbookingapi.services.RoomService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public RoomEntity saveRoom(RoomDTO roomDTO, HotelEntity hotel) {
        log.info("Attempting to save a new room: {}", roomDTO);
        RoomEntity room = mapRoomDtoToRoom(roomDTO, hotel);
        room = roomRepository.save(room);
        log.info("Successfully saved room with ID: {}", room.getId());
        return room;
    }

    @Override
    public List<RoomEntity> saveRooms(List<RoomDTO> roomDTOs, HotelEntity hotel) {
        log.info("Attempting to save rooms: {}", roomDTOs);
        List<RoomEntity> rooms = roomDTOs.stream()
                .map(roomDTO -> saveRoom(roomDTO, hotel)) // save each room
                .collect(Collectors.toList());
        log.info("Successfully saved rooms: {}", rooms);
        return rooms;
    }

    @Override
    public Optional<RoomEntity> findRoomById(Long id) {
        return roomRepository.findById(id);
    }

    @Override
    public RoomEntity updateRoom(RoomDTO roomDTO) {
        log.info("Attempting to update room with ID: {}", roomDTO.getId());
        RoomEntity existingRoom = roomRepository.findById(roomDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));

        log.info("Room with ID: {} found", roomDTO.getId());

        existingRoom.setRoomType(roomDTO.getRoomType());
        existingRoom.setRoomCount(roomDTO.getRoomCount());
        existingRoom.setPricePerNight(roomDTO.getPricePerNight());

        RoomEntity updatedRoom = roomRepository.save(existingRoom);
        log.info("Successfully updated address with ID: {}", existingRoom.getId());

        return updatedRoom;
    }

    @Override
    public void deleteRoom(Long id) {

    }

    @Override
    public RoomEntity mapRoomDtoToRoom(RoomDTO roomDTO, HotelEntity hotel) {
        log.debug("Mapping RoomDTO to Room: {}", roomDTO);
        RoomEntity room = RoomEntity.builder()
                .hotel(hotel)
                .roomType(roomDTO.getRoomType())
                .roomCount(roomDTO.getRoomCount())
                .pricePerNight(roomDTO.getPricePerNight())
                .build();
        log.debug("Mapped Room: {}", room);

        return room;

    }

    @Override
    public RoomDTO mapRoomToRoomDto(RoomEntity room) {
        return RoomDTO.builder()
                .id(room.getId())
                .hotelId(room.getHotel().getId())
                .roomType(room.getRoomType())
                .roomCount(room.getRoomCount())
                .pricePerNight(room.getPricePerNight())
                .build();
    }
}
