package com.bookingmx.reservations.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationModelTest {

    @Test
    void defaultStatusIsActive_andIsActiveReflectsStatus() {
        Reservation r = new Reservation(1L, "Guest", "Hotel",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        assertEquals(ReservationStatus.ACTIVE, r.getStatus());
        assertTrue(r.isActive());

        r.setStatus(ReservationStatus.CANCELED);
        assertEquals(ReservationStatus.CANCELED, r.getStatus());
        assertFalse(r.isActive());
    }

    @Test
    void equalsAndHashCodeById() {
        Reservation r1 = new Reservation(10L, "G", "H",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        Reservation r2 = new Reservation(10L, "Gx", "Hx",
                LocalDate.now().plusDays(3), LocalDate.now().plusDays(4));
        Reservation r3 = new Reservation(11L, "G", "H",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertNotEquals(r1, r3);
    }
}
