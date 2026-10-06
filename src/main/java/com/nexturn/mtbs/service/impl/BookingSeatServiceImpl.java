package com.nexturn.mtbs.service.impl;

import com.nexturn.mtbs.entity.BookingSeat;
import com.nexturn.mtbs.exception.BookingNotFoundException;
import com.nexturn.mtbs.repository.BookingSeatRepository;
import com.nexturn.mtbs.service.BookingSeatService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingSeatServiceImpl implements BookingSeatService {

    private final BookingSeatRepository bookingSeatRepository;

    public BookingSeatServiceImpl(BookingSeatRepository bookingSeatRepository) {
        this.bookingSeatRepository = bookingSeatRepository;
    }

    @Override
    public BookingSeat createBookingSeat(BookingSeat bookingSeat) {
        return bookingSeatRepository.save(bookingSeat);
    }

    @Override
    public BookingSeat getBookingSeatById(Long id) {
        return bookingSeatRepository.findById(id)
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking seat not found with id: " + id));
    }

    @Override
    public List<BookingSeat> getAllBookingSeats() {
        return bookingSeatRepository.findAll();
    }

    @Override
    public BookingSeat updateBookingSeat(Long id, BookingSeat bookingSeat) {

        BookingSeat existingBookingSeat = getBookingSeatById(id);

        existingBookingSeat.setBooking(bookingSeat.getBooking());
        existingBookingSeat.setSeat(bookingSeat.getSeat());
        existingBookingSeat.setPrice(bookingSeat.getPrice());

        return bookingSeatRepository.save(existingBookingSeat);
    }

    @Override
    public void deleteBookingSeat(Long id) {

        BookingSeat existingBookingSeat = getBookingSeatById(id);

        bookingSeatRepository.delete(existingBookingSeat);
    }
}