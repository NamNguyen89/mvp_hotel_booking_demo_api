package com.namnguyen.mvphotelbookingapi.controller;

import com.namnguyen.mvphotelbookingapi.models.dto.BookingDTO;
import com.namnguyen.mvphotelbookingapi.models.dto.HotelDTO;
import com.namnguyen.mvphotelbookingapi.models.dto.HotelRegistrationDTO;
import com.namnguyen.mvphotelbookingapi.services.BookingService;
import com.namnguyen.mvphotelbookingapi.services.HotelService;
import com.namnguyen.mvphotelbookingapi.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
@Slf4j
public class HotelManagerController {

    private final HotelService hotelService;
    private final UserService userService;
    private final BookingService bookingService;


    @PostMapping("/hotels")
    public ResponseEntity<Object> addHotel(@RequestBody HotelRegistrationDTO hotelRegistrationDTO) {
        hotelService.saveHotel(hotelRegistrationDTO);
        return ResponseEntity.ok("Hotel added successfully");
    }

    @GetMapping("/hotels")
    public ResponseEntity<Object> listHotels() {
        try {
            Long managerId = getCurrentManagerId();
            List<HotelDTO> hotelList = hotelService.findAllHotelDtosByManagerId(managerId);
            return ResponseEntity.ok(hotelList);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hotels not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping("/hotels/{id}")
    public ResponseEntity<Object> viewHotelDetails(@PathVariable Long id) {
        try {
            Long managerId = getCurrentManagerId();
            HotelDTO hotelDTO = hotelService.findHotelByIdAndManagerId(id, managerId);
            return ResponseEntity.ok(hotelDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Hotel not found");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    @PutMapping("/hotels/{id}")
    public ResponseEntity<Object> editHotel(@PathVariable Long id,
                                            @RequestBody HotelDTO hotelDTO) {
        try {
            Long managerId = getCurrentManagerId();
            hotelDTO.setId(id);
            hotelService.updateHotelByManagerId(hotelDTO, managerId);
            return ResponseEntity.ok("Hotel updated successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Hotel not found");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/hotels/{id}")
    public ResponseEntity<Object> deleteHotel(@PathVariable Long id) {
        try {
            Long managerId = getCurrentManagerId();
            hotelService.deleteHotelByIdAndManagerId(id, managerId);
            return ResponseEntity.ok("Hotel deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Hotel not found");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    @GetMapping("/bookings")
    public ResponseEntity<Object> listBookings() {
        try {
            Long managerId = getCurrentManagerId();
            List<BookingDTO> bookingDTOs = bookingService.findBookingsByManagerId(managerId);
            return ResponseEntity.ok(bookingDTOs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bookings not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<Object> viewBookingDetails(@PathVariable Long id) {
        try {
            Long managerId = getCurrentManagerId();
            BookingDTO bookingDTO = bookingService.findBookingByIdAndManagerId(id, managerId);
            return ResponseEntity.ok(bookingDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    private Long getCurrentManagerId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findUserByUsername(username).getHotelManager().getId();
    }
}
