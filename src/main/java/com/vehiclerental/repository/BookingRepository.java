package com.vehiclerental.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vehiclerental.entity.Booking;
import com.vehiclerental.entity.Vehicle;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByVehicle(Vehicle vehicle);

    List<Booking> findByVehicleAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Vehicle vehicle,
            LocalDate endDate,
            LocalDate startDate
    );

    List<Booking> findByUserId(Long userId);
}