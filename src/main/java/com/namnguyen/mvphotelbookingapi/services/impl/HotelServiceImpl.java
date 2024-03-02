package com.namnguyen.mvphotelbookingapi.services.impl;

import com.namnguyen.mvphotelbookingapi.models.dto.AddressDTO;
import com.namnguyen.mvphotelbookingapi.models.dto.HotelDTO;
import com.namnguyen.mvphotelbookingapi.models.dto.HotelRegistrationDTO;
import com.namnguyen.mvphotelbookingapi.models.dto.RoomDTO;
import com.namnguyen.mvphotelbookingapi.models.entity.AddressEntity;
import com.namnguyen.mvphotelbookingapi.models.entity.HotelEntity;
import com.namnguyen.mvphotelbookingapi.models.entity.HotelManagerEntity;
import com.namnguyen.mvphotelbookingapi.models.entity.RoomEntity;
import com.namnguyen.mvphotelbookingapi.repository.HotelRepository;
import com.namnguyen.mvphotelbookingapi.services.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {

    //services
    private final RoomService roomService;
    private final UserService userService;
    private final AddressService addressService;
    private final HotelManagerService hotelManagerService;

    //repository
    private final HotelRepository hotelRepository;

    @Override
    @Transactional
    public HotelEntity saveHotel(HotelRegistrationDTO hotelRegistrationDTO) {
        log.info("Attempting to save a new hotel: {}", hotelRegistrationDTO.toString());

        Optional<HotelEntity> existingHotel = hotelRepository.findByName(hotelRegistrationDTO.getName());
        if (existingHotel.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"This hotel name is already registered!");
        }

        HotelEntity hotel = mapHotelRegistrationDtoToHotel(hotelRegistrationDTO);

        AddressEntity savedAddress = addressService.saveAddress(hotelRegistrationDTO.getAddressDTO());
        hotel.setAddress(savedAddress);

        // Get the username of the currently logged-in hotel manager
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Retrieve the Hotel Manager associated with this username
        HotelManagerEntity hotelManager = hotelManagerService.findByUser(userService.findUserByUsername(username));
        hotel.setHotelManager(hotelManager);

        // Saving hotel to be able to bind rooms to hotel id
        hotel = hotelRepository.save(hotel);

        List<RoomEntity> savedRooms = roomService.saveRooms(hotelRegistrationDTO.getRoomDTOs(), hotel);
        hotel.setRooms(savedRooms);

        HotelEntity savedHotel = hotelRepository.save(hotel);
        log.info("Successfully saved new hotel with ID: {}", hotel.getId());
        return savedHotel;
    }

    @Override
    public HotelDTO findHotelDtoByName(String name) {
        HotelEntity hotel = hotelRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
        return mapHotelToHotelDto(hotel);
    }

    @Override
    public HotelDTO findHotelDtoById(Long id) {
        HotelEntity hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));

        return mapHotelToHotelDto(hotel);
    }

    @Override
    public Optional<HotelEntity> findHotelById(Long id) {
        return hotelRepository.findById(id);
    }

    @Override
    public List<HotelDTO> findAllHotels() {
        List<HotelEntity> hotels = hotelRepository.findAll();
        return hotels.stream()
                .map(this::mapHotelToHotelDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public HotelDTO updateHotel(HotelDTO hotelDTO) {
        log.info("Attempting to update hotel with ID: {}", hotelDTO.getId());

        HotelEntity existingHotel = hotelRepository.findById(hotelDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));

        if (hotelNameExistsAndNotSameHotel(hotelDTO.getName(), hotelDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This hotel name is already registered!");
        }

        existingHotel.setName(hotelDTO.getName());

        AddressEntity updatedAddress = addressService.updateAddress(hotelDTO.getAddressDTO());
        existingHotel.setAddress(updatedAddress);

        hotelDTO.getRoomDTOs().forEach(roomService::updateRoom);

        hotelRepository.save(existingHotel);
        log.info("Successfully updated existing hotel with ID: {}", hotelDTO.getId());
        return mapHotelToHotelDto(existingHotel);
    }

    @Override
    public void deleteHotelById(Long id) {
        hotelRepository.deleteById(id);
        log.info("Successfully deleted hotel with ID: {}", id);
    }
    @Override
    public List<HotelEntity> findAllHotelsByManagerId(Long managerId) {
        List<HotelEntity> hotels = hotelRepository.findAllByHotelManager_Id(managerId);

        return (hotels != null)
                ? hotels
                : Collections.emptyList();
    }

    @Override
    public List<HotelDTO> findAllHotelDtosByManagerId(Long managerId) {
        List<HotelEntity> hotels = hotelRepository.findAllByHotelManager_Id(managerId);
        if (hotels != null) {
            return hotels.stream()
                    .map(this::mapHotelToHotelDto)
                    .collect(Collectors.toList());
        }


        return Collections.emptyList();
    }

    @Override
    public HotelDTO findHotelByIdAndManagerId(Long hotelId, Long managerId) {
        HotelEntity hotel = hotelRepository.findByIdAndHotelManager_Id(hotelId, managerId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));

        return mapHotelToHotelDto(hotel);
    }

    @Override
    @Transactional
    public HotelDTO updateHotelByManagerId(HotelDTO hotelDTO, Long managerId) {
        log.info("Attempting to update hotel with ID: {} for Manager ID: {}", hotelDTO.getId(), managerId);

        HotelEntity existingHotel = hotelRepository.findByIdAndHotelManager_Id(hotelDTO.getId(), managerId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));

        if (hotelNameExistsAndNotSameHotel(hotelDTO.getName(), hotelDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This hotel name is already registered!");
        }

        existingHotel.setName(hotelDTO.getName());
        AddressEntity updatedAddress = addressService.updateAddress(hotelDTO.getAddressDTO());
        existingHotel.setAddress(updatedAddress);
        hotelDTO.getRoomDTOs().forEach(roomService::updateRoom);
        hotelRepository.save(existingHotel);

        log.info("Successfully updated existing hotel with ID: {} for Manager ID: {}", hotelDTO.getId(), managerId);
        return mapHotelToHotelDto(existingHotel);
    }

    @Override
    public void deleteHotelByIdAndManagerId(Long hotelId, Long managerId) {
        log.info("Attempting to delete hotel with ID: {} for Manager ID: {}", hotelId, managerId);
        HotelEntity hotel = hotelRepository.findByIdAndHotelManager_Id(hotelId, managerId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));

        hotelRepository.delete(hotel);
        log.info("Successfully deleted hotel with ID: {} for Manager ID: {}", hotelId, managerId);
    }

    private HotelEntity mapHotelRegistrationDtoToHotel(HotelRegistrationDTO dto) {
        return HotelEntity.builder()
                .name(formatText(dto.getName()))
                .build();
    }

    @Override
    public HotelDTO mapHotelToHotelDto(HotelEntity hotel) {
        List<RoomDTO> roomDTOs = hotel.getRooms().stream()
                .map(roomService::mapRoomToRoomDto)  // convert each Room to RoomDTO
                .collect(Collectors.toList());  // collect results to a list

        AddressDTO addressDTO = addressService.mapAddressToAddressDto(hotel.getAddress());

        return HotelDTO.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .addressDTO(addressDTO)
                .roomDTOs(roomDTOs)
                .managerUsername(hotel.getHotelManager().getUser().getUsername())
                .build();
    }

    private boolean hotelNameExistsAndNotSameHotel(String name, Long hotelId) {
        Optional<HotelEntity> existingHotelWithSameName = hotelRepository.findByName(name);
        return existingHotelWithSameName.isPresent() && !existingHotelWithSameName.get().getId().equals(hotelId);
    }

    private String formatText(String text) {
        return StringUtils.capitalize(text.trim());
    }

}

