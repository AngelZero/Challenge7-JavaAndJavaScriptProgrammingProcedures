package com.bookingmx.reservations.service;

import com.bookingmx.reservations.dto.ReservationRequest;
import com.bookingmx.reservations.model.Reservation;
import com.bookingmx.reservations.model.ReservationStatus;
import com.bookingmx.reservations.repo.ReservationRepository;
import com.bookingmx.reservations.exception.BadRequestException;
import com.bookingmx.reservations.exception.NotFoundException;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


/**
 * Business logic for reservations: list, create, update, cancel.
 * Enforces date validation and status rules before delegating to the repository.
 */
@Service
public class ReservationService {
    private final ReservationRepository repo = new ReservationRepository();

    /**
     * Returns all reservations.
     * @return list of reservations (possibly empty)
     */
    public List<Reservation> list() {
        return repo.findAll();
    }

    /**
     * Creates a reservation after validating date rules.
     * @param req reservation data (guest, hotel, check-in, check-out)
     * @return persisted reservation with generated id and ACTIVE status
     * @throws BadRequestException if any date is null, not in the future, or check-out is not after check-in
     */
    public Reservation create(ReservationRequest req) {
        validateDates(req.getCheckIn(), req.getCheckOut());
        Reservation r = new Reservation(null, req.getGuestName(), req.getHotelName(), req.getCheckIn(), req.getCheckOut());
        return repo.save(r);
    }

    /**
     * Updates an existing reservation if it exists and is ACTIVE.
     * @param id reservation id
     * @param req new values (guest, hotel, check-in, check-out)
     * @return updated reservation
     * @throws NotFoundException if the reservation does not exist
     * @throws BadRequestException if the reservation is CANCELED or dates are invalid
     */
    public Reservation update(Long id, ReservationRequest req) {
        Reservation existing = repo.findById(id).orElseThrow(() -> new NotFoundException("Reservation not found"));
        if (!existing.isActive()) throw new BadRequestException("Cannot update a canceled reservation");
        validateDates(req.getCheckIn(), req.getCheckOut());
        existing.setGuestName(req.getGuestName());
        existing.setHotelName(req.getHotelName());
        existing.setCheckIn(req.getCheckIn());
        existing.setCheckOut(req.getCheckOut());
        return repo.save(existing);
    }

    /**
     * Cancels a reservation by setting status=CANCELED.
     * @param id reservation id
     * @return updated reservation with CANCELED status
     * @throws NotFoundException if the reservation does not exist
     */
    public Reservation cancel(Long id) {
        Reservation existing = repo.findById(id).orElseThrow(() -> new NotFoundException("Reservation not found"));
        existing.setStatus(ReservationStatus.CANCELED);
        return repo.save(existing);
    }

    /**
     * Validates business rules for dates.
     * @param in  check-in date
     * @param out check-out date
     * @throws BadRequestException if null, out ≤ in, or either date is not in the future
     */
    private void validateDates(LocalDate in, LocalDate out) {
        if (in == null || out == null) throw new BadRequestException("Dates cannot be null");
        if (!out.isAfter(in)) throw new BadRequestException("Check-out must be after check-in");
        if (in.isBefore(LocalDate.now())) throw new BadRequestException("Check-in must be in the future");
        if (out.isBefore(LocalDate.now())) throw new BadRequestException("Check-out must be in the future");
    }
}
