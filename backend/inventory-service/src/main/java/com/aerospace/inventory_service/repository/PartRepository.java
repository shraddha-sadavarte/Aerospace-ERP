package com.aerospace.inventory_service.repository;

import java.util.List;
import java.util.Optional;

// Fixed: Correct Pageable import
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page; // Added for the return type
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aerospace.inventory_service.entity.Part;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {
    
    Optional<Part> findBySerialNumber(String serialNumber);  
    
    List<Part> findByNameContainingIgnoreCase(String name);
    
    List<Part> findByPartType(String type);

    // Standard Spring Data JPA pagination method
    Page<Part> findAll(Pageable pageable);

    @Query("SELECT SUM(p.quantity) FROM Part p")
    Long getTotalQuantity();

    @Query("SELECT COUNT(p) FROM Part p WHERE p.quantity > 0 AND p.quantity <= p.reorderLevel")
    long countLowStock();

    @Query("SELECT COUNT(p) FROM Part p WHERE p.quantity = 0")
    long countOutOfStock();

    @Query("SELECT p.partType, COUNT(p) FROM Part p GROUP BY p.partType")
    List<Object[]> countPartsByType();
}
