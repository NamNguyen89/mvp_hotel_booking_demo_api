package com.namnguyen.mvphotelbookingapi.services.impl;


import com.namnguyen.mvphotelbookingapi.models.dto.AddressDTO;
import com.namnguyen.mvphotelbookingapi.models.dto.BookingDTO;
import com.namnguyen.mvphotelbookingapi.models.dto.BookingInitiationDTO;
import com.namnguyen.mvphotelbookingapi.models.dto.RoomSelectionDTO;
import com.namnguyen.mvphotelbookingapi.models.entity.*;
import com.namnguyen.mvphotelbookingapi.repository.BookingRepository;
import com.namnguyen.mvphotelbookingapi.services.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final HotelService hotelService;
    private final PaymentService paymentService;
    private final CustomerService customerService;
    private final AvailabilityService availabilityService;

    private final BookingRepository bookingRepository;


    @Override
    @Transactional
    public BookingEntity saveBooking(BookingInitiationDTO bookingInitiationDTO, Long userId) {
        validateBookingDates(bookingInitiationDTO.getCheckinDate(), bookingInitiationDTO.getCheckoutDate());

        CustomerEntity customer = customerService.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with user ID: " + userId));

        HotelEntity hotel = hotelService.findHotelById(bookingInitiationDTO.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with ID: " + bookingInitiationDTO.getHotelId()));

        BookingEntity booking = mapBookingInitDtoToBookingModel(bookingInitiationDTO, customer, hotel);

        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public BookingDTO confirmBooking(BookingInitiationDTO bookingInitiationDTO, Long customerId) {
        BookingEntity savedBooking = saveBooking(bookingInitiationDTO, customerId);
        PaymentEntity savedPayment = paymentService.savePayment(bookingInitiationDTO, savedBooking);
        savedBooking.setPayment(savedPayment);
        bookingRepository.save(savedBooking);

        availabilityService.updateAvailabilities(bookingInitiationDTO.getHotelId(), bookingInitiationDTO.getCheckinDate(),
                bookingInitiationDTO.getCheckoutDate(), bookingInitiationDTO.getRoomSelections());

        return mapBookingModelToBookingDto(savedBooking);
    }
//
//    @Override
//    public List<BookingDTO> findAllBookings() {
//        List<Booking> bookings = bookingRepository.findAll();
//        return bookings.stream()
//                .map(this::mapBookingModelToBookingDto)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public BookingDTO findBookingById(Long bookingId) {
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + bookingId));
//        return mapBookingModelToBookingDto(booking);
//    }

    @Override
    public List<BookingDTO> findBookingsByCustomerId(Long customerId) {
        List<BookingEntity> bookingDTOs = bookingRepository.findBookingsByCustomerId(customerId);

        return bookingDTOs.stream()
                .map(this::mapBookingModelToBookingDto)
                .sorted(Comparator.comparing(BookingDTO::getCheckinDate))
                .collect(Collectors.toList());
    }

    @Override
    public BookingDTO findBookingByIdAndCustomerId(Long bookingId, Long customerId) {
        BookingEntity booking = bookingRepository.findBookingByIdAndCustomerId(bookingId, customerId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + bookingId));

        return mapBookingModelToBookingDto(booking);
    }

    @Override
    public List<BookingDTO> findBookingsByManagerId(Long managerId) {
        List<HotelEntity> hotels = hotelService.findAllHotelsByManagerId(managerId);

        return hotels.stream()
                .flatMap(hotel -> bookingRepository.findBookingsByHotelId(hotel.getId()).stream())
                .map(this::mapBookingModelToBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDTO findBookingByIdAndManagerId(Long bookingId, Long managerId) {
        BookingEntity booking = bookingRepository.findBookingByIdAndHotel_HotelManagerId(bookingId, managerId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + bookingId + " and manager ID: " + managerId));

        return mapBookingModelToBookingDto(booking);
    }

    private void validateBookingDates(LocalDate checkinDate, LocalDate checkoutDate) {
        if (checkinDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }

        if (checkoutDate.isBefore(checkinDate.plusDays(1))) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
    }

    @Override
    public BookingDTO mapBookingModelToBookingDto(BookingEntity booking) {
        AddressDTO addressDto = AddressDTO.builder()
                .addressLine(booking.getHotel().getAddress().getAddressLine())
                .city(booking.getHotel().getAddress().getCity())
                .country(booking.getHotel().getAddress().getCountry())
                .build();

        List<RoomSelectionDTO> roomSelections = booking.getBookedRooms().stream()
                .map(room -> RoomSelectionDTO.builder()
                        .roomType(room.getRoomType())
                        .count(room.getCount())
                        .build())
                .collect(Collectors.toList());

        UserEntity customerUser = booking.getCustomer().getUser();

        return BookingDTO.builder()
                .id(booking.getId())
                .confirmationNumber(booking.getConfirmationNumber())
                .bookingDate(booking.getBookingDate())
                .customerId(booking.getCustomer().getId())
                .hotelId(booking.getHotel().getId())
                .checkinDate(booking.getCheckinDate())
                .checkoutDate(booking.getCheckoutDate())
                .roomSelections(roomSelections)
                .totalPrice(booking.getPayment().getTotalPrice())
                .hotelName(booking.getHotel().getName())
                .hotelAddress(addressDto)
                .customerName(customerUser.getName() + " " + customerUser.getLastName())
                .customerEmail(customerUser.getUsername())
                .paymentStatus(booking.getPayment().getPaymentStatus())
                .paymentMethod(booking.getPayment().getPaymentMethod())
                .build();
    }

    private BookingEntity mapBookingInitDtoToBookingModel(BookingInitiationDTO bookingInitiationDTO,
                                                          CustomerEntity customer,
                                                          HotelEntity hotel) {
        BookingEntity booking = BookingEntity.builder()
                .customer(customer)
                .hotel(hotel)
                .checkinDate(bookingInitiationDTO.getCheckinDate())
                .checkoutDate(bookingInitiationDTO.getCheckoutDate())
                .build();

        for (RoomSelectionDTO roomSelection : bookingInitiationDTO.getRoomSelections()) {
            if (roomSelection.getCount() > 0) {
                BookedRoomEntity bookedRoom = BookedRoomEntity.builder()
                        .booking(booking)
                        .roomType(roomSelection.getRoomType())
                        .count(roomSelection.getCount())
                        .build();

                booking.getBookedRooms().add(bookedRoom);
            }
        }

        return booking;
    }
}
