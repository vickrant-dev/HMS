package hms.controller;

import hms.model.Reservation;

@FunctionalInterface
public interface DashboardObserver {

    void onReservationCreated(Reservation reservation);

    default void onCheckIn(Reservation reservation) {
    }

    default void onCheckOut(Reservation reservation) {
    }
}
