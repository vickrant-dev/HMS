package hms.service;

import hms.model.Room;

/**
 * Strategy interface for room pricing calculations.
 */
@FunctionalInterface
public interface PricingStrategy {

    /**
     * Calculates the price for a room stay.
     *
     * @param room The room being priced
     * @param numberOfNights The number of nights
     * @return The calculated price
     */
    double calculatePrice(Room room, int numberOfNights);
}
