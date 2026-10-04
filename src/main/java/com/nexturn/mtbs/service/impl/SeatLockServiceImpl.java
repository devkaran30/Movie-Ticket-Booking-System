package com.nexturn.mtbs.service.impl;

import com.nexturn.mtbs.entity.SeatLock;
import com.nexturn.mtbs.enums.BookingStatus;
import com.nexturn.mtbs.enums.SeatLockStatus;
import com.nexturn.mtbs.repository.BookingSeatRepository;
import com.nexturn.mtbs.repository.SeatLockRepository;
import com.nexturn.mtbs.service.SeatLockService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SeatLockServiceImpl implements SeatLockService {

    private final SeatLockRepository seatLockRepository;
    private final BookingSeatRepository bookingSeatRepository;

    public SeatLockServiceImpl(
            SeatLockRepository seatLockRepository,
            BookingSeatRepository bookingSeatRepository) {

        this.seatLockRepository = seatLockRepository;
        this.bookingSeatRepository = bookingSeatRepository;
    }

    @Override
    public SeatLock createSeatLock(SeatLock seatLock) {
        return seatLockRepository.save(seatLock);
    }

    @Override
    public SeatLock lockSeat(SeatLock seatLock) {

        Long seatId = seatLock.getSeat().getId();
        Long showId = seatLock.getShow().getId();

        LocalDateTime currentTime = LocalDateTime.now();

        // ---------------------------------------------------------
        // 1. Check if the seat is already booked for this show
        // ---------------------------------------------------------

        var existingBooking =
                bookingSeatRepository
                        .findBySeatIdAndBooking_Show_IdAndBooking_BookingStatus(
                                seatId,
                                showId,
                                BookingStatus.CONFIRMED
                        );

        if (!existingBooking.isEmpty()) {

            throw new RuntimeException(
                    "Seat " + seatId +
                    " is already booked for show " + showId
            );
        }

        // ---------------------------------------------------------
        // 2. Check if the seat is currently locked
        // ---------------------------------------------------------

        var existingLock =
                seatLockRepository
                        .findBySeatIdAndShowIdAndStatusAndExpiresAtAfter(
                                seatId,
                                showId,
                                SeatLockStatus.LOCKED,
                                currentTime
                        );

        if (existingLock.isPresent()) {

            throw new RuntimeException(
                    "Seat " + seatId +
                    " is already locked for show " + showId
            );
        }

        // ---------------------------------------------------------
        // 3. Check for an old expired lock
        // ---------------------------------------------------------

        var oldLock =
                seatLockRepository
                        .findBySeatIdAndShowIdAndStatus(
                                seatId,
                                showId,
                                SeatLockStatus.LOCKED
                        );

        if (oldLock.isPresent()
                && oldLock.get().getExpiresAt().isBefore(currentTime)) {

            oldLock.get().setStatus(SeatLockStatus.EXPIRED);

            seatLockRepository.save(oldLock.get());
        }

        // ---------------------------------------------------------
        // 4. Set lock details
        // ---------------------------------------------------------

        seatLock.setLockedAt(currentTime);

        if (seatLock.getStatus() == null) {
            seatLock.setStatus(SeatLockStatus.LOCKED);
        }

        // ---------------------------------------------------------
        // 5. Save the seat lock
        // ---------------------------------------------------------

        return seatLockRepository.save(seatLock);
    }

    @Override
    public SeatLock getSeatLockById(Long id) {

        return seatLockRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Seat lock not found with id: " + id
                        ));
    }

    @Override
    public List<SeatLock> getAllSeatLocks() {
        return seatLockRepository.findAll();
    }

    @Override
    public SeatLock updateSeatLock(
            Long id,
            SeatLock seatLock) {

        SeatLock existingSeatLock =
                getSeatLockById(id);

        existingSeatLock.setSeat(seatLock.getSeat());
        existingSeatLock.setUser(seatLock.getUser());
        existingSeatLock.setShow(seatLock.getShow());
        existingSeatLock.setLockedAt(seatLock.getLockedAt());
        existingSeatLock.setExpiresAt(seatLock.getExpiresAt());
        existingSeatLock.setStatus(seatLock.getStatus());

        return seatLockRepository.save(existingSeatLock);
    }

    @Override
    public void deleteSeatLock(Long id) {

        SeatLock existingSeatLock =
                getSeatLockById(id);

        seatLockRepository.delete(existingSeatLock);
    }
}