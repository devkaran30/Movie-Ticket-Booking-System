package com.nexturn.mtbs.service.impl;

import com.nexturn.mtbs.dto.request.PaymentRequest;
import com.nexturn.mtbs.entity.Booking;
import com.nexturn.mtbs.entity.Payment;
import com.nexturn.mtbs.entity.SeatLock;
import com.nexturn.mtbs.entity.Ticket;
import com.nexturn.mtbs.enums.BookingStatus;
import com.nexturn.mtbs.enums.PaymentStatus;
import com.nexturn.mtbs.enums.SeatLockStatus;
import com.nexturn.mtbs.enums.TicketStatus;
import com.nexturn.mtbs.repository.BookingRepository;
import com.nexturn.mtbs.repository.PaymentRepository;
import com.nexturn.mtbs.repository.SeatLockRepository;
import com.nexturn.mtbs.repository.TicketRepository;
import com.nexturn.mtbs.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;
    private final SeatLockRepository seatLockRepository;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            BookingRepository bookingRepository,
            TicketRepository ticketRepository,
            SeatLockRepository seatLockRepository) {

        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.ticketRepository = ticketRepository;
        this.seatLockRepository = seatLockRepository;
    }

    @Override
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public Payment processPayment(PaymentRequest paymentRequest) {

        // 1. Validate request
        if (paymentRequest.getBookingId() == null) {
            throw new RuntimeException("Booking ID is required");
        }

        if (paymentRequest.getTransactionId() == null ||
                paymentRequest.getTransactionId().isBlank()) {
            throw new RuntimeException("Transaction ID is required");
        }

        if (paymentRequest.getAmount() == null ||
                paymentRequest.getAmount().signum() <= 0) {
            throw new RuntimeException("Payment amount must be greater than zero");
        }

        if (paymentRequest.getPaymentMethod() == null) {
            throw new RuntimeException("Payment method is required");
        }

        // 2. Find booking
        Booking booking = bookingRepository.findById(
                paymentRequest.getBookingId()
        ).orElseThrow(() ->
                new RuntimeException(
                        "Booking not found with id: "
                                + paymentRequest.getBookingId()
                )
        );

        // 3. Check booking status
        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new RuntimeException(
                    "Payment cannot be processed. Booking status is "
                            + booking.getBookingStatus()
            );
        }

        // 4. Check payment amount
        if (paymentRequest.getAmount()
                .compareTo(booking.getTotalAmount()) != 0) {

            throw new RuntimeException(
                    "Payment amount does not match booking amount"
            );
        }

        // 5. Create payment
        Payment payment = new Payment();

        payment.setBooking(booking);
        payment.setTransactionId(paymentRequest.getTransactionId());
        payment.setAmount(paymentRequest.getAmount());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());

        // For our simulated payment flow
        payment.setPaymentStatus(PaymentStatus.SUCCESS);

        payment.setPaymentTime(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        // 6. Payment successful → confirm booking
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        // 7. Generate ticket
        Ticket ticket = new Ticket();

        ticket.setTicketNumber(
                "TKT" + System.currentTimeMillis()
        );

        ticket.setBooking(booking);
        ticket.setTicketStatus(TicketStatus.ACTIVE);
        ticket.setIssueTime(LocalDateTime.now());

        ticketRepository.save(ticket);

        // 8. Release seat locks
        List<SeatLock> seatLocks =
                seatLockRepository.findAll();

        for (SeatLock seatLock : seatLocks) {

            if (seatLock.getShow().getId()
                    .equals(booking.getShow().getId())
                    && seatLock.getUser().getId()
                    .equals(booking.getUser().getId())
                    && seatLock.getStatus()
                    == SeatLockStatus.LOCKED) {

                seatLock.setStatus(SeatLockStatus.RELEASED);
                seatLockRepository.save(seatLock);
            }
        }

        return savedPayment;
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment not found with id: " + id
                        )
                );
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment updatePayment(Long id, Payment payment) {

        Payment existingPayment = getPaymentById(id);

        existingPayment.setBooking(payment.getBooking());
        existingPayment.setTransactionId(payment.getTransactionId());
        existingPayment.setAmount(payment.getAmount());
        existingPayment.setPaymentMethod(payment.getPaymentMethod());
        existingPayment.setPaymentStatus(payment.getPaymentStatus());
        existingPayment.setPaymentTime(payment.getPaymentTime());

        return paymentRepository.save(existingPayment);
    }

    @Override
    public void deletePayment(Long id) {

        Payment existingPayment = getPaymentById(id);

        paymentRepository.delete(existingPayment);
    }
}