package com.admin.service;

import com.admin.exception.ServiceNotFoundException;
import com.admin.model.Service;
import com.admin.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marks this class as a Spring service.
public class ServiceService {

    private final ServiceRepository serviceRepository;

    @Autowired // Autowires the repository for use in this class.
    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    // Retrieve all services
    public List<Service> getAllServices() {
        return serviceRepository.findAll(); // Find all services in the database.
    }

    // Retrieve a service by ID with error handling
    public Service getServiceById(Long id) throws ServiceNotFoundException {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Could not find any service with ID " + id));
    }

    // Create a new service
    public Service createService(Service service) {
        return serviceRepository.save(service); // Save the new service to the database.
    }

    // Update a service by ID with error handling
    public Service updateService(Long id, Service updatedService) throws ServiceNotFoundException {
        Service existingService = getServiceById(id); // Find service by ID or throw an exception.

        // Update fields in the existing service
        existingService.setName(updatedService.getName());
        existingService.setDescription(updatedService.getDescription());
        existingService.setPrice(updatedService.getPrice());

        return serviceRepository.save(existingService); // Save changes to the database.
    }

    // Delete a service by ID with error handling
    public void deleteService(Long id) throws ServiceNotFoundException {
        if (!serviceRepository.existsById(id)) {
            throw new ServiceNotFoundException("Could not find any service with ID " + id);
        }
        serviceRepository.deleteById(id); // Delete service from the database.
    }
}