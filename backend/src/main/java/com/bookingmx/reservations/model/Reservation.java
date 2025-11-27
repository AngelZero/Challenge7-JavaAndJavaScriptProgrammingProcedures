package com.bookingmx.reservations.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Domain model for a reservation.
 * Equality and hash code are based on the {@code id} field.
 */
public class Reservation {
    private Long id;
    private String guestName;
    private String hotelName;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private ReservationStatus status = ReservationStatus.ACTIVE;

    public Reservation() {}

    public Reservation(Long id, String guestName, String hotelName, LocalDate checkIn, LocalDate checkOut) {
        this.id = id;
        this.guestName = guestName;
        this.hotelName = hotelName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = ReservationStatus.ACTIVE;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }
    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }
    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }

    public boolean isActive() { return this.status == ReservationStatus.ACTIVE; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override public int hashCode() { return Objects.hash(id); }
}
