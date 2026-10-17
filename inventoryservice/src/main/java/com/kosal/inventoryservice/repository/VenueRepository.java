package com.kosal.inventoryservice.repository;

import com.kosal.inventoryservice.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Long> {
}
