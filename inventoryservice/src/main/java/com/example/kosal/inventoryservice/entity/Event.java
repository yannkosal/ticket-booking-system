package com.example.kosal.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "event")
public class Event {
    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "total_capacity")
    private Long total_capacity;

    @Column(name = "left_capacity")
    private Long left_capacity;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;
}
