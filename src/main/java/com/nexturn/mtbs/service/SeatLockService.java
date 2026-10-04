package com.nexturn.mtbs.service;

import com.nexturn.mtbs.entity.SeatLock;

import java.util.List;

public interface SeatLockService {

    SeatLock createSeatLock(SeatLock seatLock);

    SeatLock lockSeat(SeatLock seatLock);

    SeatLock getSeatLockById(Long id);

    List<SeatLock> getAllSeatLocks();

    SeatLock updateSeatLock(Long id, SeatLock seatLock);

    void deleteSeatLock(Long id);
}