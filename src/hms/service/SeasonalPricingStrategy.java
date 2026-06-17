package hms.service;

import hms.model.Room;

/**
 * Pricing strategy that applies a seasonal multiplier to the base price.
 */
public final class SeasonalPricingStrategy implements PricingStrategy {

    private final double seasonalMultiplier;

    /**
     * Creates a seasonal pricing strategy.
     *
     * @param seasonalMultiplier The multiplier to apply (e.g., 1.5 for peak season)
     */
    public SeasonalPricingStrategy(double seasonalMultiplier) {
        this.seasonalMultiplier = seasonalMultiplier;
    }

    /**
     * Calculates price as base price multiplied by nights and seasonal multiplier.
     *
     * @param room The room being priced
     * @param numberOfNights The number of nights
     * @return The calculated price
     */
    @Override
    public double calculatePrice(Room room, int numberOfNights) {
        return room.getBasePrice() * numberOfNights * seasonalMultiplier;
    }
}
