package com.nexturn.mtbs.service;

import com.nexturn.mtbs.entity.BookingSeat;

import java.util.List;

public interface BookingSeatService {

    BookingSeat createBookingSeat(BookingSeat bookingSeat);

    BookingSeat getBookingSeatById(Long id);

    List<BookingSeat> getAllBookingSeats();

    BookingSeat updateBookingSeat(Long id, BookingSeat bookingSeat);

    void deleteBookingSeat(Long id);
}