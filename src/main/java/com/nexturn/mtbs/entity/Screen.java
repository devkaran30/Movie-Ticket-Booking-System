package com.nexturn.mtbs.entity;

import com.nexturn.mtbs.enums.ScreenStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "screens")
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theatre_id", nullable = false)
    private Theatre theatre;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer totalSeats;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ScreenStatus status;

    // Default constructor
    public Screen() {
    }

    // Parameterized constructor
    public Screen(Long id, Theatre theatre, String name,
                  Integer totalSeats, ScreenStatus status) {
        this.id = id;
        this.theatre = theatre;
        this.name = name;
        this.totalSeats = totalSeats;
        this.status = status;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Theatre getTheatre() {
        return theatre;
    }

    public void setTheatre(Theatre theatre) {
        this.theatre = theatre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public ScreenStatus getStatus() {
        return status;
    }

    public void setStatus(ScreenStatus status) {
        this.status = status;
    }
}