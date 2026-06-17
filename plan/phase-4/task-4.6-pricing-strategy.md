# Task 4.6 — Pricing Strategy Pattern

**Package:** `hms.service`
**Files:**
- `src/hms/service/PricingStrategy.java` (interface)
- `src/hms/service/NormalPricingStrategy.java`
- `src/hms/service/SeasonalPricingStrategy.java`
- `src/hms/service/CorporatePricingStrategy.java`

## Description
Strategy pattern for room pricing. Follows code_rules.md §12.4.

## Methods

| File | Method | Logic |
|------|--------|-------|
| `PricingStrategy` | `calculatePrice(Room, int)` | Interface method |
| `NormalPricingStrategy` | `calculatePrice(Room, int)` | basePrice × nights |
| `SeasonalPricingStrategy` | `calculatePrice(Room, int)` | basePrice × nights × seasonalMultiplier |
| `CorporatePricingStrategy` | `calculatePrice(Room, int)` | basePrice × nights × (1 − discountRate) |

## Dependencies
- hms.model.Room (Phase 2.2)

## Checklist
- [x] Create checklist file
- [x] Write `PricingStrategy.java`
- [x] Write `NormalPricingStrategy.java`
- [x] Write `SeasonalPricingStrategy.java`
- [x] Write `CorporatePricingStrategy.java`
- [x] Verify Clean & Build
