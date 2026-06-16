package hms.service;

import hms.model.Room;

public final class NormalPricingStrategy implements PricingStrategy {

    @Override
    public double calculatePrice(Room room, int numberOfNights) {
        return room.getBasePrice() * numberOfNights;
    }
}
