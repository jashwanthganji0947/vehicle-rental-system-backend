package com.vehiclerental.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vehiclerental.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByAvailableTrue();

    List<Vehicle> findByType(String type);

    List<Vehicle> findByBrand(String brand);
}