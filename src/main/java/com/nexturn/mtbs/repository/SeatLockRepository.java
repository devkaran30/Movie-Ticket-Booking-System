package com.nexturn.mtbs.repository;

import com.nexturn.mtbs.entity.SeatLock;
import com.nexturn.mtbs.enums.SeatLockStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SeatLockRepository extends JpaRepository<SeatLock, Long> {

    Optional<SeatLock> findBySeatIdAndShowIdAndStatus(
            Long seatId,
            Long showId,
            SeatLockStatus status
    );

    Optional<SeatLock> findBySeatIdAndShowIdAndStatusAndExpiresAtAfter(
            Long seatId,
            Long showId,
            SeatLockStatus status,
            LocalDateTime currentTime
    );

    Optional<SeatLock> findBySeatIdAndShowIdAndUserIdAndStatusAndExpiresAtAfter(
            Long seatId,
            Long showId,
            Long userId,
            SeatLockStatus status,
            LocalDateTime currentTime
    );
}