package com.bookingmx.reservations.repo;

import com.bookingmx.reservations.model.Reservation;
import com.bookingmx.reservations.model.ReservationStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ReservationRepositoryTest {

    @Test
    void save_findAll_findById_delete() {
        ReservationRepository repo = new ReservationRepository();

        Reservation a = new Reservation(null, "A", "H1",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        Reservation b = new Reservation(null, "B", "H2",
                LocalDate.now().plusDays(3), LocalDate.now().plusDays(4));

        // save assigns ids
        a = repo.save(a);
        b = repo.save(b);
        assertNotNull(a.getId());
        assertNotNull(b.getId());

        // findAll
        assertEquals(2, repo.findAll().size());

        // findById present
        Optional<Reservation> found = repo.findById(a.getId());
        assertTrue(found.isPresent());
        assertEquals("A", found.get().getGuestName());
        assertEquals(ReservationStatus.ACTIVE, found.get().getStatus());

        // delete and then not found
        repo.delete(a.getId());
        assertEquals(1, repo.findAll().size());
        assertTrue(repo.findById(a.getId()).isEmpty());
    }
}
