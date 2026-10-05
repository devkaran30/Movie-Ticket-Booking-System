package com.nexturn.mtbs.service.impl;

import com.nexturn.mtbs.dto.request.BookingRequest;
import com.nexturn.mtbs.entity.Booking;
import com.nexturn.mtbs.entity.BookingSeat;
import com.nexturn.mtbs.entity.Payment;
import com.nexturn.mtbs.entity.Refund;
import com.nexturn.mtbs.entity.Seat;
import com.nexturn.mtbs.entity.SeatLock;
import com.nexturn.mtbs.entity.Show;
import com.nexturn.mtbs.entity.Ticket;
import com.nexturn.mtbs.entity.User;
import com.nexturn.mtbs.enums.BookingStatus;
import com.nexturn.mtbs.enums.PaymentStatus;
import com.nexturn.mtbs.enums.RefundStatus;
import com.nexturn.mtbs.enums.SeatLockStatus;
import com.nexturn.mtbs.enums.SeatStatus;
import com.nexturn.mtbs.enums.TicketStatus;
import com.nexturn.mtbs.repository.BookingRepository;
import com.nexturn.mtbs.repository.BookingSeatRepository;
import com.nexturn.mtbs.repository.PaymentRepository;
import com.nexturn.mtbs.repository.RefundRepository;
import com.nexturn.mtbs.repository.SeatLockRepository;
import com.nexturn.mtbs.repository.SeatRepository;
import com.nexturn.mtbs.repository.ShowRepository;
import com.nexturn.mtbs.repository.TicketRepository;
import com.nexturn.mtbs.repository.UserRepository;
import com.nexturn.mtbs.service.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final SeatLockRepository seatLockRepository;
    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;
    private final TicketRepository ticketRepository;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            BookingSeatRepository bookingSeatRepository,
            SeatLockRepository seatLockRepository,
            SeatRepository seatRepository,
            ShowRepository showRepository,
            UserRepository userRepository,
            PaymentRepository paymentRepository,
            RefundRepository refundRepository,
            TicketRepository ticketRepository) {

        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.seatLockRepository = seatLockRepository;
        this.seatRepository = seatRepository;
        this.showRepository = showRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.refundRepository = refundRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking createRealBooking(BookingRequest bookingRequest) {

        if (bookingRequest.getUserId() == null) {
            throw new RuntimeException("User ID is required");
        }

        if (bookingRequest.getShowId() == null) {
            throw new RuntimeException("Show ID is required");
        }

        if (bookingRequest.getSeatIds() == null ||
                bookingRequest.getSeatIds().isEmpty()) {

            throw new RuntimeException("At least one seat is required");
        }

        if (bookingRequest.getTotalAmount() == null ||
                bookingRequest.getTotalAmount().signum() <= 0) {

            throw new RuntimeException(
                    "Booking amount must be greater than zero"
            );
        }

        User user = userRepository.findById(bookingRequest.getUserId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: "
                                        + bookingRequest.getUserId()
                        )
                );

        Show show = showRepository.findById(bookingRequest.getShowId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Show not found with id: "
                                        + bookingRequest.getShowId()
                        )
                );

        LocalDateTime currentTime = LocalDateTime.now();

        // Check every selected seat
        for (Long seatId : bookingRequest.getSeatIds()) {

            seatRepository.findById(seatId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Seat not found with id: " + seatId
                            )
                    );

            Optional<SeatLock> activeLock =
                    seatLockRepository
                            .findBySeatIdAndShowIdAndUserIdAndStatusAndExpiresAtAfter(
                                    seatId,
                                    bookingRequest.getShowId(),
                                    bookingRequest.getUserId(),
                                    SeatLockStatus.LOCKED,
                                    currentTime
                            );

            if (activeLock.isEmpty()) {

                throw new RuntimeException(
                        "Seat " + seatId +
                                " is not locked by user " +
                                bookingRequest.getUserId() +
                                " for show " +
                                bookingRequest.getShowId()
                );
            }
        }

        // Create the booking
        Booking booking = new Booking();

        booking.setBookingNumber(
                "BK" + System.currentTimeMillis()
        );

        booking.setUser(user);
        booking.setShow(show);

        booking.setTotalAmount(
                bookingRequest.getTotalAmount()
        );

        booking.setBookingStatus(
                BookingStatus.PENDING
        );

        booking.setBookingTime(currentTime);

        Booking savedBooking =
                bookingRepository.save(booking);

        // Create BookingSeat records
        for (Long seatId : bookingRequest.getSeatIds()) {

            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Seat not found with id: " + seatId
                            )
                    );

            BookingSeat bookingSeat = new BookingSeat();

            bookingSeat.setBooking(savedBooking);
            bookingSeat.setSeat(seat);
            bookingSeat.setPrice(show.getTicketPrice());

            bookingSeatRepository.save(bookingSeat);
        }

        return savedBooking;
    }

    @Override
    @Transactional
    public Booking cancelBooking(Long bookingId) {

        Booking booking = getBookingById(bookingId);

        if (booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException(
                    "Only confirmed bookings can be cancelled"
            );
        }

        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment not found for booking id: "
                                        + bookingId
                        )
                );

        if (payment.getPaymentStatus() != PaymentStatus.SUCCESS) {
            throw new RuntimeException(
                    "Booking cannot be cancelled because payment is not successful"
            );
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);

        Refund refund = new Refund();

        refund.setPayment(payment);
        refund.setRefundTransactionId(
                "REF" + System.currentTimeMillis()
        );
        refund.setRefundAmount(payment.getAmount());
        refund.setRefundReason("Customer cancelled the booking");
        refund.setRefundStatus(RefundStatus.PROCESSED);
        refund.setRefundTime(LocalDateTime.now());

        refundRepository.save(refund);

        Ticket ticket = ticketRepository.findByBookingId(bookingId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Ticket not found for booking id: "
                                        + bookingId
                        )
                );

        ticket.setTicketStatus(TicketStatus.CANCELLED);
        ticketRepository.save(ticket);

        List<BookingSeat> bookingSeats =
                bookingSeatRepository.findByBookingId(bookingId);

        for (BookingSeat bookingSeat : bookingSeats) {

            Seat seat = bookingSeat.getSeat();

            seat.setStatus(SeatStatus.AVAILABLE);

            seatRepository.save(seat);
        }

        return booking;
    }

    @Override
    public Booking getBookingById(Long id) {

        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found with id: " + id
                        )
                );
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<Booking> getBookingsByUserId(Long userId) {

        return bookingRepository.findByUserId(userId);
    }

    @Override
    public Booking updateBooking(Long id, Booking booking) {

        Booking existingBooking = getBookingById(id);

        existingBooking.setBookingNumber(
                booking.getBookingNumber()
        );

        existingBooking.setUser(
                booking.getUser()
        );

        existingBooking.setShow(
                booking.getShow()
        );

        existingBooking.setTotalAmount(
                booking.getTotalAmount()
        );

        existingBooking.setBookingStatus(
                booking.getBookingStatus()
        );

        existingBooking.setBookingTime(
                booking.getBookingTime()
        );

        return bookingRepository.save(existingBooking);
    }

    @Override
    public void deleteBooking(Long id) {

        Booking existingBooking = getBookingById(id);

        bookingRepository.delete(existingBooking);
    }
}