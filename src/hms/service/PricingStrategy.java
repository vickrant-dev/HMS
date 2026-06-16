package hms.service;

import hms.model.Room;

@FunctionalInterface
public interface PricingStrategy {

    double calculatePrice(Room room, int numberOfNights);
}
