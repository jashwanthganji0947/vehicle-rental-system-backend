package com.vehiclerental.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vehiclerental.entity.Booking;
import com.vehiclerental.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Simulate payment for a booking
    @PostMapping("/{bookingId}")
    public ResponseEntity<Booking> makePayment(
            @PathVariable Long bookingId) {

        Booking booking = paymentService.makePayment(bookingId);

        return ResponseEntity.ok(booking);
    }
}