package com.vehiclerental.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vehiclerental.entity.Vehicle;
import com.vehiclerental.repository.VehicleRepository;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        vehicle.setAvailable(true);
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public List<Vehicle> getAvailableVehicles() {
        return vehicleRepository.findByAvailableTrue();
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }

    public Vehicle updateVehicle(Long id, Vehicle vehicleDetails) {

        Vehicle vehicle = getVehicleById(id);

        vehicle.setName(vehicleDetails.getName());
        vehicle.setBrand(vehicleDetails.getBrand());
        vehicle.setModel(vehicleDetails.getModel());
        vehicle.setType(vehicleDetails.getType());
        vehicle.setRegistrationNumber(
                vehicleDetails.getRegistrationNumber()
        );
        vehicle.setPricePerDay(vehicleDetails.getPricePerDay());
        vehicle.setAvailable(vehicleDetails.isAvailable());
        vehicle.setDescription(vehicleDetails.getDescription());

        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id) {

        Vehicle vehicle = getVehicleById(id);

        vehicleRepository.delete(vehicle);
    }

    public List<Vehicle> getVehiclesByType(String type) {
        return vehicleRepository.findByType(type);
    }

    public List<Vehicle> getVehiclesByBrand(String brand) {
        return vehicleRepository.findByBrand(brand);
    }
}