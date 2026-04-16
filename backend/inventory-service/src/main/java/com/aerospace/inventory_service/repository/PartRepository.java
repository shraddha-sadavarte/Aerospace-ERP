package com.aerospace.inventory_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aerospace.inventory_service.entity.Part;

@Repository
public interface PartRepository extends JpaRepository<Part, Long > {
    Optional<Part> findBySerialNumber(String serialNumber);  
    List<Part> findByNameContainingIgnoreCase(String name);
    List<Part> findByPartType(String type);

}
