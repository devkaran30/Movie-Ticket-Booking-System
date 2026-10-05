package com.nexturn.mtbs.service.impl;

import com.nexturn.mtbs.entity.Ticket;
import com.nexturn.mtbs.repository.TicketRepository;
import com.nexturn.mtbs.service.TicketService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Ticket not found with id: " + id
                        )
                );
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public List<Ticket> getTicketsByUserId(Long userId) {
        return ticketRepository.findByBookingUserId(userId);
    }

    @Override
    public Ticket updateTicket(Long id, Ticket ticket) {

        Ticket existingTicket = getTicketById(id);

        existingTicket.setTicketNumber(ticket.getTicketNumber());
        existingTicket.setBooking(ticket.getBooking());
        existingTicket.setTicketStatus(ticket.getTicketStatus());
        existingTicket.setIssueTime(ticket.getIssueTime());

        return ticketRepository.save(existingTicket);
    }

    @Override
    public void deleteTicket(Long id) {

        Ticket existingTicket = getTicketById(id);

        ticketRepository.delete(existingTicket);
    }
}