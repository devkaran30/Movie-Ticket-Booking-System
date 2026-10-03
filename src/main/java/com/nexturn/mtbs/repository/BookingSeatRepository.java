package com.nexturn.mtbs.repository;

import com.nexturn.mtbs.entity.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {
}