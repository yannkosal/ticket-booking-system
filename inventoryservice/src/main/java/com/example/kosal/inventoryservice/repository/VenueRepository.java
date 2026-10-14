package com.example.kosal.inventoryservice.repository;

import com.example.kosal.inventoryservice.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Long> {
}
