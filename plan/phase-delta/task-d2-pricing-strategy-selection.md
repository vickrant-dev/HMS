# Task D2 — Pricing Strategy Selection Logic

**Area:** Phase 4.6 — Pricing Strategy Pattern (completion)  
**Severity:** MEDIUM — `SeasonalPricingStrategy` and `CorporatePricingStrategy` are dead code

## Problem

Both `ReservationController.calculateTotalAmount()` and `BillingController.generateBill()` hardcode:

```java
PricingStrategy strategy = new NormalPricingStrategy();
```

The three strategy classes are fully implemented, but neither controller contains logic to select the right one based on guest type or booking date:

| Strategy | Constructor | Instantiated? |
|----------|-------------|---------------|
| `NormalPricingStrategy` | `()` | Yes (in both controllers) |
| `SeasonalPricingStrategy` | `(double seasonalMultiplier)` | **No** |
| `CorporatePricingStrategy` | `(double discountRate)` | **No** |

## Changes Required

### 1. `src/hms/controller/ReservationController.java`

Add a private helper method:

```java
private PricingStrategy selectPricingStrategy(Guest guest, LocalDate checkIn) {
    if (guest == null) return new NormalPricingStrategy();
    if ("Corporate".equals(guest.getGuestType())) {
        return new CorporatePricingStrategy(0.15); // 15% corporate discount
    }
    // Peak season: Jun-Aug, Dec
    int month = checkIn.getMonthValue();
    if (month >= 6 && month <= 8 || month == 12) {
        return new SeasonalPricingStrategy(1.5); // 50% peak surcharge
    }
    return new NormalPricingStrategy();
}
```

Update `calculateTotalAmount()` to accept the guest and use the helper.

### 2. `src/hms/controller/BillingController.java`

Update `generateBill()` to accept `Guest` parameter and use `selectPricingStrategy()`. Or extract the strategy selection to a shared utility if preferred.

### Impact analysis

- `ReservationController.calculateTotalAmount()` currently takes `(Room room, long nights)` — needs `(Guest guest, Room room, long nights)` overload
- Callers in `NewReservationDialog` must be updated to pass the selected guest
- `BillingController.generateBill()` currently takes `(Reservation reservation)` — needs to pass guest from reservation

## Verification

1. Create a reservation for a Corporate guest → verify 15% discount applied
2. Create a reservation during June → verify 50% peak surcharge applied
3. Create a reservation for Regular guest in off-peak → verify normal pricing
4. Run billing for each case → verify consistent pricing

## Checklist

- [ ] Add `selectPricingStrategy()` helper to `ReservationController`
- [ ] Update `calculateTotalAmount()` signature + callers to pass guest
- [ ] Update `BillingController.generateBill()` to use guest-aware strategy
- [ ] Update `NewReservationDialog` to pass guest to pricing call
- [ ] Update `GuestCheckOutDialog` if it directly calls pricing
- [ ] Verify Clean & Build
