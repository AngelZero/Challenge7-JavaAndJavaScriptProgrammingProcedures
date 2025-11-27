package com.bookingmx.reservations.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationDtoTest {

    @Test
    void gettersAndSetters() {
        ReservationRequest req = new ReservationRequest();
        req.setGuestName("Alice");
        req.setHotelName("Hotel");
        req.setCheckIn(LocalDate.now().plusDays(5));
        req.setCheckOut(req.getCheckIn().plusDays(2));

        assertEquals("Alice", req.getGuestName());
        assertEquals("Hotel", req.getHotelName());
        assertNotNull(req.getCheckIn());
        assertNotNull(req.getCheckOut());
    }
}
