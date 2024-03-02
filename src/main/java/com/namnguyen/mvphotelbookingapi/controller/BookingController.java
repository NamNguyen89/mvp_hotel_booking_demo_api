package com.namnguyen.mvphotelbookingapi.controller;

import com.namnguyen.mvphotelbookingapi.models.dto.BookingDTO;
import com.namnguyen.mvphotelbookingapi.models.dto.BookingInitiationDTO;
import com.namnguyen.mvphotelbookingapi.models.dto.HotelDTO;
import com.namnguyen.mvphotelbookingapi.models.dto.UserDTO;
import com.namnguyen.mvphotelbookingapi.services.BookingService;
import com.namnguyen.mvphotelbookingapi.services.HotelService;
import com.namnguyen.mvphotelbookingapi.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final UserService userService;
    private final HotelService hotelService;
    private final BookingService bookingService;


    @PostMapping("/payment")
    public ResponseEntity<Object> showPaymentPage(@RequestBody BookingInitiationDTO bookingInitiationDTO) {
        if (bookingInitiationDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Booking initiation DTO is null");
        }

        HotelDTO hotelDTO = hotelService.findHotelDtoById(bookingInitiationDTO.getHotelId());
        return ResponseEntity.ok(hotelDTO);
    }

    @PostMapping("/payment")
    public ResponseEntity<Object> confirmBooking(@RequestBody BookingInitiationDTO bookingInitiationDTO,
                                                 BindingResult result) {
        if (bookingInitiationDTO == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Your session has expired. Please start a new search.");
        }

        if (result.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Validation errors occurred while completing booking");
        }

        try {
            Long userId = getLoggedInUserId();
            BookingDTO bookingDTO = bookingService.confirmBooking(bookingInitiationDTO, userId);
            return ResponseEntity.ok(bookingDTO);
        } catch (Exception e) {
            log.error("An error occurred while completing the booking", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again later.");
        }
    }

    @GetMapping("/confirmation")
    public ResponseEntity<Object> showConfirmationPage(@RequestBody BookingDTO bookingDTO) {
        if (bookingDTO == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Booking DTO is null");
        }

        LocalDate checkinDate = bookingDTO.getCheckinDate();
        LocalDate checkoutDate = bookingDTO.getCheckoutDate();
        long durationDays = ChronoUnit.DAYS.between(checkinDate, checkoutDate);

        return ResponseEntity.ok("Duration of stay: " + durationDays
                + " days. Booking details: " + bookingDTO);
    }

    private Long getLoggedInUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();
        UserDTO userDTO = userService.findUserDTOByUsername(username);
        log.info("Fetched logged in user ID: {}", userDTO.getId());
        return userDTO.getId();
    }
}