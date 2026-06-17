package hms.service;

import hms.model.Room;

/**
 * Pricing strategy that applies a corporate discount to the base price.
 */
public final class CorporatePricingStrategy implements PricingStrategy {

    private final double discountRate;

    /**
     * Creates a corporate pricing strategy.
     *
     * @param discountRate The discount rate (e.g., 0.15 for 15% off)
     */
    public CorporatePricingStrategy(double discountRate) {
        this.discountRate = discountRate;
    }

    /**
     * Calculates price as base price multiplied by nights and minus the discount.
     *
     * @param room The room being priced
     * @param numberOfNights The number of nights
     * @return The calculated price
     */
    @Override
    public double calculatePrice(Room room, int numberOfNights) {
        double baseAmount = room.getBasePrice() * numberOfNights;
        return baseAmount * (1 - discountRate);
    }
}
