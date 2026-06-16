package hms.service;

import hms.model.Room;

public final class SeasonalPricingStrategy implements PricingStrategy {

    private final double seasonalMultiplier;

    public SeasonalPricingStrategy(double seasonalMultiplier) {
        this.seasonalMultiplier = seasonalMultiplier;
    }

    @Override
    public double calculatePrice(Room room, int numberOfNights) {
        return room.getBasePrice() * numberOfNights * seasonalMultiplier;
    }
}
