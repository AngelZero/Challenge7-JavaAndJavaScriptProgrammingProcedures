package com.bookingmx.reservations.service;

import com.bookingmx.reservations.dto.ReservationRequest;
import com.bookingmx.reservations.exception.BadRequestException;
import com.bookingmx.reservations.exception.NotFoundException;
import com.bookingmx.reservations.model.Reservation;
import com.bookingmx.reservations.model.ReservationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    private ReservationService service;

    @BeforeEach
    void setUp() {
        service = new ReservationService();
    }

    private ReservationRequest req(String guest, String hotel, LocalDate in, LocalDate out) {
        ReservationRequest r = new ReservationRequest();
        r.setGuestName(guest);
        r.setHotelName(hotel);
        r.setCheckIn(in);
        r.setCheckOut(out);
        return r;
    }

    @Test
    void list_initiallyEmpty() {
        List<Reservation> all = service.list();
        assertNotNull(all);
        assertTrue(all.isEmpty(), "Expected empty list on fresh service");
    }

    @Test
    void create_success_persistsAndReturnsActive() {
        LocalDate in = LocalDate.now().plusDays(5);
        LocalDate out = in.plusDays(2);

        Reservation created = service.create(req("Alice", "Hotel Azul", in, out));

        assertNotNull(created.getId(), "ID should be assigned");
        assertEquals("Alice", created.getGuestName());
        assertEquals("Hotel Azul", created.getHotelName());
        assertEquals(in, created.getCheckIn());
        assertEquals(out, created.getCheckOut());
        assertEquals(ReservationStatus.ACTIVE, created.getStatus());
    }

    @Test
    void create_rejectsNullDates() {
        LocalDate future = LocalDate.now().plusDays(3);

        // null check-in
        assertThrows(BadRequestException.class, () ->
                service.create(req("Bob", "Hotel Verde", null, future))
        );

        // null check-out
        assertThrows(BadRequestException.class, () ->
                service.create(req("Bob", "Hotel Verde", future, null))
        );
    }

    @Test
    void create_rejectsCheckoutNotAfterCheckin() {
        LocalDate in = LocalDate.now().plusDays(7);

        // check-out == check-in
        assertThrows(BadRequestException.class, () ->
                service.create(req("Carol", "Hotel Rojo", in, in))
        );

        // check-out before check-in
        assertThrows(BadRequestException.class, () ->
                service.create(req("Carol", "Hotel Rojo", in, in.minusDays(1)))
        );
    }

    @Test
    void create_rejectsPastCheckin() {
        LocalDate past = LocalDate.now().minusDays(1);
        LocalDate future = LocalDate.now().plusDays(2);

        assertThrows(BadRequestException.class, () ->
                service.create(req("Dan", "Hotel Naranja", past, future))
        );
    }

    @Test
    void list_afterCreates_returnsAll() {
        LocalDate in1 = LocalDate.now().plusDays(2), out1 = in1.plusDays(2);
        LocalDate in2 = LocalDate.now().plusDays(5), out2 = in2.plusDays(3);

        service.create(req("Eva", "Hotel Uno", in1, out1));
        service.create(req("Fran", "Hotel Dos", in2, out2));

        List<Reservation> all = service.list();
        assertEquals(2, all.size(), "Expected two reservations after two creates");
    }

    @Test
    void update_success_updatesFields() {
        LocalDate in = LocalDate.now().plusDays(4), out = in.plusDays(3);
        Reservation created = service.create(req("Gus", "Hotel Tres", in, out));

        LocalDate newIn = LocalDate.now().plusDays(10), newOut = newIn.plusDays(5);
        Reservation updated = service.update(
                created.getId(),
                req("Gustavo", "Hotel Tres Deluxe", newIn, newOut)
        );

        assertEquals(created.getId(), updated.getId());
        assertEquals("Gustavo", updated.getGuestName());
        assertEquals("Hotel Tres Deluxe", updated.getHotelName());
        assertEquals(newIn, updated.getCheckIn());
        assertEquals(newOut, updated.getCheckOut());
        assertEquals(ReservationStatus.ACTIVE, updated.getStatus());
    }

    @Test
    void update_nonExistentId_throwsNotFound() {
        LocalDate in = LocalDate.now().plusDays(3), out = in.plusDays(1);
        assertThrows(NotFoundException.class, () ->
                service.update(999L, req("Helen", "Hotel Cuatro", in, out))
        );
    }

    @Test
    void update_canceledReservation_throwsBadRequest() {
        LocalDate in = LocalDate.now().plusDays(3), out = in.plusDays(2);
        Reservation created = service.create(req("Ivan", "Hotel Cinco", in, out));

        service.cancel(created.getId());

        LocalDate newIn = LocalDate.now().plusDays(6), newOut = newIn.plusDays(1);
        assertThrows(BadRequestException.class, () ->
                service.update(created.getId(), req("Ivan", "Hotel Cinco", newIn, newOut))
        );
    }

    @Test
    void cancel_success_setsStatusCanceled() {
        LocalDate in = LocalDate.now().plusDays(3), out = in.plusDays(2);
        Reservation created = service.create(req("Julia", "Hotel Seis", in, out));

        Reservation canceled = service.cancel(created.getId());

        assertEquals(ReservationStatus.CANCELED, canceled.getStatus());
    }

    @Test
    void cancel_nonExistentId_throwsNotFound() {
        assertThrows(NotFoundException.class, () -> service.cancel(12345L));
    }
}
