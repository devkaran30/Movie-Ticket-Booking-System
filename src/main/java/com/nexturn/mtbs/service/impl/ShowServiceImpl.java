package com.nexturn.mtbs.service.impl;

import com.nexturn.mtbs.dto.response.SeatResponse;
import com.nexturn.mtbs.dto.response.ShowResponse;
import com.nexturn.mtbs.entity.Seat;
import com.nexturn.mtbs.entity.Show;
import com.nexturn.mtbs.enums.BookingStatus;
import com.nexturn.mtbs.enums.SeatLockStatus;
import com.nexturn.mtbs.repository.BookingSeatRepository;
import com.nexturn.mtbs.repository.SeatLockRepository;
import com.nexturn.mtbs.repository.SeatRepository;
import com.nexturn.mtbs.repository.ShowRepository;
import com.nexturn.mtbs.service.ShowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final SeatLockRepository seatLockRepository;

    public ShowServiceImpl(
            ShowRepository showRepository,
            SeatRepository seatRepository,
            BookingSeatRepository bookingSeatRepository,
            SeatLockRepository seatLockRepository) {

        this.showRepository = showRepository;
        this.seatRepository = seatRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.seatLockRepository = seatLockRepository;
    }

    @Override
    public Show createShow(Show show) {
        return showRepository.save(show);
    }

    @Override
    @Transactional(readOnly = true)
    public ShowResponse getShowById(Long id) {

        Show show = showRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Show not found with id: " + id
                        ));

        return toShowResponse(show);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowResponse> getAllShows() {

        return showRepository.findAll()
                .stream()
                .map(this::toShowResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowResponse> getShowsByMovieId(Long movieId) {

        return showRepository.findByMovieId(movieId)
                .stream()
                .map(this::toShowResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowResponse> getShowsByTheatreId(Long theatreId) {

        return showRepository.findByScreenTheatreId(theatreId)
                .stream()
                .map(this::toShowResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowResponse> getShowsByMovieIdAndDate(
            Long movieId,
            LocalDate showDate) {

        return showRepository
                .findByMovieIdAndShowDate(movieId, showDate)
                .stream()
                .map(this::toShowResponse)
                .toList();
    }

    @Override
    public Show updateShow(Long id, Show show) {

        Show existingShow = getShowEntityById(id);

        existingShow.setMovie(show.getMovie());
        existingShow.setScreen(show.getScreen());
        existingShow.setShowDate(show.getShowDate());
        existingShow.setStartTime(show.getStartTime());
        existingShow.setEndTime(show.getEndTime());
        existingShow.setTicketPrice(show.getTicketPrice());
        existingShow.setStatus(show.getStatus());

        return showRepository.save(existingShow);
    }

    @Override
    public void deleteShow(Long id) {

        Show existingShow = getShowEntityById(id);

        showRepository.delete(existingShow);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatResponse> getSeatsForShow(Long showId) {

        Show show = getShowEntityById(showId);

        Long screenId = show.getScreen().getId();

        List<Seat> seats =
                seatRepository.findByScreenId(screenId);

        List<SeatResponse> responses = new ArrayList<>();

        LocalDateTime currentTime = LocalDateTime.now();

        for (Seat seat : seats) {

            String seatStatus;

            if (seat.getStatus() != null
                    && seat.getStatus().name().equals("INACTIVE")) {

                seatStatus = "INACTIVE";

            } else {

                var existingBooking =
                        bookingSeatRepository
                                .findBySeatIdAndBooking_Show_IdAndBooking_BookingStatus(
                                        seat.getId(),
                                        showId,
                                        BookingStatus.CONFIRMED
                                );

                if (!existingBooking.isEmpty()) {

                    seatStatus = "BOOKED";

                } else {

                    var existingLock =
                            seatLockRepository
                                    .findBySeatIdAndShowIdAndStatusAndExpiresAtAfter(
                                            seat.getId(),
                                            showId,
                                            SeatLockStatus.LOCKED,
                                            currentTime
                                    );

                    if (existingLock.isPresent()) {
                        seatStatus = "LOCKED";
                    } else {
                        seatStatus = "AVAILABLE";
                    }
                }
            }

            SeatResponse response =
                    new SeatResponse(
                            seat.getId(),
                            seat.getSeatNumber(),
                            seat.getRowNumber(),
                            seat.getSeatType(),
                            seatStatus
                    );

            responses.add(response);
        }

        return responses;
    }

    private ShowResponse toShowResponse(Show show) {

        return new ShowResponse(
                show.getId(),

                show.getMovie().getId(),
                show.getMovie().getTitle(),

                show.getScreen().getId(),
                show.getScreen().getName(),

                show.getScreen().getTheatre().getId(),
                show.getScreen().getTheatre().getName(),

                show.getShowDate(),
                show.getStartTime(),
                show.getEndTime(),

                show.getTicketPrice(),
                show.getStatus()
        );
    }

    private Show getShowEntityById(Long id) {

        return showRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Show not found with id: " + id
                        ));
    }
}