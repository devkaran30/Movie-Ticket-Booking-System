package com.nexturn.mtbs.entity;

import com.nexturn.mtbs.enums.TicketStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_ticket_number",
                columnNames = "ticket_number"
        )
})
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_number", nullable = false, unique = true, length = 50)
    private String ticketNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketStatus ticketStatus;

    @Column(nullable = false)
    private LocalDateTime issueTime;

    public Ticket() {
    }

    public Ticket(Long id, String ticketNumber, Booking booking,
                  TicketStatus ticketStatus, LocalDateTime issueTime) {
        this.id = id;
        this.ticketNumber = ticketNumber;
        this.booking = booking;
        this.ticketStatus = ticketStatus;
        this.issueTime = issueTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public LocalDateTime getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(LocalDateTime issueTime) {
        this.issueTime = issueTime;
    }
}