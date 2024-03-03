package com.namnguyen.mvphotelbookingapi.controller;

import com.namnguyen.mvphotelbookingapi.models.dto.HotelAvailabilityDTO;
import com.namnguyen.mvphotelbookingapi.models.dto.HotelSearchDTO;
import com.namnguyen.mvphotelbookingapi.services.HotelSearchService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/searching")
@RequiredArgsConstructor
@Slf4j
public class HotelSearchController {

    private final HotelSearchService hotelSearchService;

    @PostMapping("/hotels")
    public ResponseEntity<Object> findAvailableHotelsByCityAndDate(@RequestBody HotelSearchDTO hotelSearchDTO) {
        try {
            validateCheckinAndCheckoutDates(hotelSearchDTO.getCheckinDate(), hotelSearchDTO.getCheckoutDate());
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }

        try {
            List<HotelAvailabilityDTO> hotels = hotelSearchService.findAvailableHotelsByCityAndDate(hotelSearchDTO.getCity(),
                    hotelSearchDTO.getCheckinDate(),
                    hotelSearchDTO.getCheckoutDate());

            if (hotels.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No hotels found");
            }

            return ResponseEntity.ok(hotels);
        } catch (Exception e) {
            log.error("An error occurred while searching for hotels", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    @GetMapping("/hotels/{id}")
    public ResponseEntity<Object> findHotelDetails(@PathVariable Long id,
                                                   @RequestParam String checkinDate,
                                                   @RequestParam String checkoutDate) {
        try {
            LocalDate parsedCheckinDate = LocalDate.parse(checkinDate);
            LocalDate parsedCheckoutDate = LocalDate.parse(checkoutDate);
            validateCheckinAndCheckoutDates(parsedCheckinDate, parsedCheckoutDate);

            HotelAvailabilityDTO hotelAvailabilityDTO = hotelSearchService.findAvailableHotelById(id, parsedCheckinDate, parsedCheckoutDate);

            return ResponseEntity.ok(hotelAvailabilityDTO);
        } catch (DateTimeParseException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid date format");
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Hotel not found");
        } catch (Exception e) {
            log.error("An error occurred while searching for hotel details", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    private void validateCheckinAndCheckoutDates(LocalDate checkinDate, LocalDate checkoutDate) {
        if (checkinDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }

        if (checkoutDate.isBefore(checkinDate.plusDays(1))) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
    }
}