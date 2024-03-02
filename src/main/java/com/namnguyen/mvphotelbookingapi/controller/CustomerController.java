package com.namnguyen.mvphotelbookingapi.controller;

import com.namnguyen.mvphotelbookingapi.models.dto.BookingDTO;
import com.namnguyen.mvphotelbookingapi.services.BookingService;
import com.namnguyen.mvphotelbookingapi.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final UserService userService;
    private final BookingService bookingService;

    @GetMapping("/bookings")
    public ResponseEntity<Object> listBookings() {
        try {
            Long customerId = getCurrentCustomerId();
            List<BookingDTO> bookingDTOs = bookingService.findBookingsByCustomerId(customerId);
            return ResponseEntity.ok(bookingDTOs);
        } catch (EntityNotFoundException e) {
            log.error("No customer found with the provided ID", e);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Customer not found. Please log in again.");
        } catch (Exception e) {
            log.error("An error occurred while listing bookings", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again later.");
        }
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<Object> viewBookingDetails(@PathVariable Long id) {
        try {
            Long customerId = getCurrentCustomerId();
            BookingDTO bookingDTO = bookingService.findBookingByIdAndCustomerId(id, customerId);
            LocalDate checkinDate = bookingDTO.getCheckinDate();
            LocalDate checkoutDate = bookingDTO.getCheckoutDate();
            long durationDays = ChronoUnit.DAYS.between(checkinDate, checkoutDate);
            Map<String, Object> response = new HashMap<>();
            response.put("bookingDTO", bookingDTO);
            response.put("days", durationDays);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            log.error("No booking found with the provided ID", e);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Booking not found. Please try again later.");
        } catch (Exception e) {
            log.error("An error occurred while displaying booking details", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again later.");
        }
    }

    private Long getCurrentCustomerId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findUserByUsername(username).getCustomer().getId();
    }


}