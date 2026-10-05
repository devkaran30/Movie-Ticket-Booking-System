package com.nexturn.mtbs.service;

import com.nexturn.mtbs.entity.Seat;
import com.nexturn.mtbs.enums.SeatStatus;

import java.util.List;

public interface SeatService {

    Seat createSeat(Seat seat);

    Seat getSeatById(Long id);

    List<Seat> getAllSeats();

    Seat updateSeat(Long id, Seat seat);

    Seat updateSeatStatus(Long id, SeatStatus status);

    void deleteSeat(Long id);
}