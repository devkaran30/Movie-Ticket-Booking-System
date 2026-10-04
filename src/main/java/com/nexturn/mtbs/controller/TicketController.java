package com.nexturn.mtbs.controller;

import com.nexturn.mtbs.dto.response.TicketResponse;
import com.nexturn.mtbs.entity.Ticket;
import com.nexturn.mtbs.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Convert Ticket entity to TicketResponse
    private TicketResponse toTicketResponse(Ticket ticket) {

        return new TicketResponse(
                ticket.getId(),
                ticket.getTicketNumber(),
                ticket.getBooking().getId(),
                ticket.getTicketStatus(),
                ticket.getIssueTime()
        );
    }

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(
            @RequestBody Ticket ticket) {

        Ticket savedTicket = ticketService.createTicket(ticket);

        return ResponseEntity.ok(
                toTicketResponse(savedTicket)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(
            @PathVariable Long id) {

        Ticket ticket = ticketService.getTicketById(id);

        return ResponseEntity.ok(
                toTicketResponse(ticket)
        );
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> getAllTickets() {

        List<TicketResponse> tickets = ticketService.getAllTickets()
                .stream()
                .map(this::toTicketResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponse> updateTicket(
            @PathVariable Long id,
            @RequestBody Ticket ticket) {

        Ticket updatedTicket =
                ticketService.updateTicket(id, ticket);

        return ResponseEntity.ok(
                toTicketResponse(updatedTicket)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(
            @PathVariable Long id) {

        ticketService.deleteTicket(id);

        return ResponseEntity.noContent().build();
    }
}