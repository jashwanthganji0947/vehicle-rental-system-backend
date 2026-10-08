package com.vehiclerental.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vehiclerental.entity.Booking;
import com.vehiclerental.entity.User;
import com.vehiclerental.repository.UserRepository;
import com.vehiclerental.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;
    private final UserRepository userRepository;

    public BookingController(
            BookingService bookingService,
            UserRepository userRepository) {

        this.bookingService = bookingService;
        this.userRepository = userRepository;
    }

    // --------------------------------------------------
    // CREATE BOOKING
    // --------------------------------------------------

    @PostMapping
    public ResponseEntity<?> createBooking(
            @RequestParam(required = false) Long userId,
            @RequestParam Long vehicleId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            Authentication authentication) {

        try {

            /*
             * Get the currently authenticated user from
             * Spring Security instead of trusting the
             * userId sent by the browser.
             */
            User currentUser =
                    userRepository.findByEmail(
                            authentication.getName()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Authenticated user not found"
                            )
                    );

            LocalDate start =
                    LocalDate.parse(startDate);

            LocalDate end =
                    LocalDate.parse(endDate);

            Booking booking =
                    bookingService.createBooking(
                            currentUser.getId(),
                            vehicleId,
                            start,
                            end
                    );

            return ResponseEntity.ok(booking);

        } catch (DateTimeParseException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Invalid date format. Please select valid dates."
                    );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // --------------------------------------------------
    // GET ALL BOOKINGS
    // ADMIN ONLY
    // --------------------------------------------------

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {

        return ResponseEntity.ok(
                bookingService.getAllBookings()
        );
    }

    // --------------------------------------------------
    // GET BOOKING BY ID
    // USER CAN ONLY SEE OWN BOOKING
    // ADMIN CAN SEE ANY BOOKING
    // --------------------------------------------------

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(
            @PathVariable Long id,
            Authentication authentication) {

        try {

            Booking booking =
                    bookingService.getBookingById(id);

            User currentUser =
                    userRepository.findByEmail(
                            authentication.getName()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Authenticated user not found"
                            )
                    );

            boolean isAdmin =
                    authentication.getAuthorities()
                            .stream()
                            .anyMatch(authority ->
                                    authority
                                            .getAuthority()
                                            .equals("ROLE_ADMIN")
                            );

            if (!isAdmin &&
                    !booking.getUser()
                            .getId()
                            .equals(currentUser.getId())) {

                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(
                                "You are not allowed to access this booking."
                        );
            }

            return ResponseEntity.ok(booking);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // --------------------------------------------------
    // GET BOOKINGS BY USER
    // USER CAN ONLY SEE OWN BOOKINGS
    // ADMIN CAN VIEW ANY USER'S BOOKINGS
    // --------------------------------------------------

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getBookingsByUser(
            @PathVariable Long userId,
            Authentication authentication) {

        try {

            User currentUser =
                    userRepository.findByEmail(
                            authentication.getName()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Authenticated user not found"
                            )
                    );

            boolean isAdmin =
                    authentication.getAuthorities()
                            .stream()
                            .anyMatch(authority ->
                                    authority
                                            .getAuthority()
                                            .equals("ROLE_ADMIN")
                            );

            if (!isAdmin &&
                    !currentUser.getId().equals(userId)) {

                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(
                                "You are not allowed to access another user's bookings."
                        );
            }

            return ResponseEntity.ok(
                    bookingService.getBookingsByUser(userId)
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}