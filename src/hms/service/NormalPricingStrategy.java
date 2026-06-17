package hms.service;

import hms.model.Room;

/**
 * Pricing strategy that calculates price as base price multiplied by number of nights.
 */
public final class NormalPricingStrategy implements PricingStrategy {

    @Override
    public double calculatePrice(Room room, int numberOfNights) {
        return room.getBasePrice() * numberOfNights;
    }
}
