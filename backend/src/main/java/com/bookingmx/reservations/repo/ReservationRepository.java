package com.bookingmx.reservations.repo;

import com.bookingmx.reservations.model.Reservation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory repository for Reservation entities.
 * Not thread-safe beyond basic ConcurrentHashMap semantics; suitable for demo/testing.
 */
public class ReservationRepository {
    private final Map<Long, Reservation> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1L);

    /** @return snapshot list of all reservations */
    public List<Reservation> findAll() {
        return new ArrayList<>(store.values());
    }

    /** @return reservation by id or Optional.empty() if not found */
    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * Persists a reservation; assigns an id when null.
     * @param r reservation to save
     * @return saved reservation (with id)
     */
    public Reservation save(Reservation r) {
        if (r.getId() == null) r.setId(seq.getAndIncrement());
        store.put(r.getId(), r);
        return r;
    }

    /** Deletes a reservation by id (no-op if absent). */
    public void delete(Long id) {
        store.remove(id);
    }
}
