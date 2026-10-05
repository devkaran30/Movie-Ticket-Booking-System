package com.nexturn.mtbs.service.impl;

import com.nexturn.mtbs.entity.Seat;
import com.nexturn.mtbs.enums.SeatStatus;
import com.nexturn.mtbs.repository.SeatRepository;
import com.nexturn.mtbs.service.SeatService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    public SeatServiceImpl(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Override
    public Seat createSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    @Override
    public Seat getSeatById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Seat not found with id: " + id
                        ));
    }

    @Override
    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    @Override
    public Seat updateSeat(Long id, Seat seat) {

        Seat existingSeat = getSeatById(id);

        existingSeat.setScreen(seat.getScreen());
        existingSeat.setSeatNumber(seat.getSeatNumber());
        existingSeat.setRowNumber(seat.getRowNumber());
        existingSeat.setSeatType(seat.getSeatType());
        existingSeat.setStatus(seat.getStatus());

        return seatRepository.save(existingSeat);
    }

    @Override
    public Seat updateSeatStatus(Long id, SeatStatus status) {

        Seat existingSeat = getSeatById(id);

        existingSeat.setStatus(status);

        return seatRepository.save(existingSeat);
    }

    @Override
    public void deleteSeat(Long id) {

        Seat existingSeat = getSeatById(id);

        seatRepository.delete(existingSeat);
    }
}