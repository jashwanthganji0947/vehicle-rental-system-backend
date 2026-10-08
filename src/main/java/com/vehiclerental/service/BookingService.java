package com.vehiclerental.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.vehiclerental.entity.Booking;
import com.vehiclerental.entity.User;
import com.vehiclerental.entity.Vehicle;
import com.vehiclerental.repository.BookingRepository;
import com.vehiclerental.repository.UserRepository;
import com.vehiclerental.repository.VehicleRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public BookingService(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            VehicleRepository vehicleRepository) {

        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public Booking createBooking(
            Long userId,
            Long vehicleId,
            LocalDate startDate,
            LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new RuntimeException("Start date and end date are required");
        }

        if (endDate.isBefore(startDate)) {
            throw new RuntimeException(
                    "End date cannot be before start date");
        }

        if (startDate.isBefore(LocalDate.now())) {
            throw new RuntimeException(
                    "Start date cannot be in the past");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() ->
                        new RuntimeException("Vehicle not found"));

        if (!vehicle.isAvailable()) {
            throw new RuntimeException(
                    "Vehicle is currently unavailable");
        }

        List<Booking> existingBookings =
                bookingRepository
                        .findByVehicleAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                                vehicle,
                                endDate,
                                startDate
                        );

        if (!existingBookings.isEmpty()) {
            throw new RuntimeException(
                    "Vehicle is already booked for the selected dates");
        }

        long numberOfDays =
                ChronoUnit.DAYS.between(startDate, endDate);

        // Minimum rental period is 1 day
        if (numberOfDays == 0) {
            numberOfDays = 1;
        }

        double totalAmount =
                numberOfDays * vehicle.getPricePerDay();

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setVehicle(vehicle);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setTotalAmount(totalAmount);

        // Payment will be handled in the next module
        booking.setPaymentStatus("PENDING");

        // Booking becomes CONFIRMED after successful payment
        booking.setBookingStatus("PENDING");

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {

        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));
    }

    public List<Booking> getBookingsByUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        return bookingRepository.findByUserId(userId);
    }
}