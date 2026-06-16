package hms.service;

import hms.model.Room;

public final class CorporatePricingStrategy implements PricingStrategy {

    private final double discountRate;

    public CorporatePricingStrategy(double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public double calculatePrice(Room room, int numberOfNights) {
        double baseAmount = room.getBasePrice() * numberOfNights;
        return baseAmount * (1 - discountRate);
    }
}
