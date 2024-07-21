package com.gotham.hotel.service.interfac;

import com.gotham.hotel.dto.Response;
import com.gotham.hotel.entity.Booking;

public interface IBookingService {
    
    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(Long bookingId);
}
