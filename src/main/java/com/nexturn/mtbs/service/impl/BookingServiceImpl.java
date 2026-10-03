package com.nexturn.mtbs.service.impl;

import com.nexturn.mtbs.entity.Booking;
import com.nexturn.mtbs.repository.BookingRepository;
import com.nexturn.mtbs.service.BookingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found with id: " + id
                        ));
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
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