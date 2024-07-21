package com.gotham.hotel.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gotham.hotel.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long>{

    List<Booking> findByRoomId(Long roomId);

    Optional<Booking> findByBookingConfirmationCode(String bookingConfirmationCode);

    List<Booking> findByUserId(Long userId);
    
}
