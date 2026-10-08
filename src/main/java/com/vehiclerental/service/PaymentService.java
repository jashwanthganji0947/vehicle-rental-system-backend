package com.vehiclerental.service;

import org.springframework.stereotype.Service;

import com.vehiclerental.entity.Booking;
import com.vehiclerental.repository.BookingRepository;

@Service
public class PaymentService {

    private final BookingRepository bookingRepository;

    public PaymentService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking makePayment(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        // Check if payment has already been completed
        if ("PAID".equals(booking.getPaymentStatus())) {
            throw new RuntimeException(
                    "Payment has already been completed");
        }

        // Simulated payment
        booking.setPaymentStatus("PAID");

        // Confirm the booking after successful payment
        booking.setBookingStatus("CONFIRMED");

        return bookingRepository.save(booking);
    }
}