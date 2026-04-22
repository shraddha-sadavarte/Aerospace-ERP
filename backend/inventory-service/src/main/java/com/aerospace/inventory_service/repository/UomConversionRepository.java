package com.aerospace.inventory_service.repository;

import com.aerospace.inventory_service.entity.UomConversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UomConversionRepository extends JpaRepository<UomConversion, Long> {

    Optional<UomConversion> findByFromUomAndToUom(String fromUom, String toUom);

}