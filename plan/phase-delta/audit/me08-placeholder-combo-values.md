# ME08 — exportCmb / reportTypeCmb Show "Item 1–4" Placeholders

**Severity:** MEDIUM  
**Files:** `src/hms/view/panels/BillingManagementPanel.java`, `src/hms/view/panels/ReportsPanel.java`

## Problem

Two combo boxes still have the default NetBeans placeholder values:
- `BillingManagementPanel.exportCmb`: `{"Item 1", "Item 2", "Item 3", "Item 4"}`
- `ReportsPanel.reportTypeCmb`: `{"Item 1", "Item 2", "Item 3", "Item 4"}`

The `exportCmb` handler shows a placeholder dialog "Export as X will be implemented." The `reportTypeCmb` handler is empty (`// TODO`).

**Impact:** Useless UI elements that confuse the user.

## Fix

For `exportCmb`:
- Replace with actual export formats: `"CSV"`, `"PDF"`, `"Excel"`
- Or wire the export functionality

For `reportTypeCmb`:
- Replace with actual report types (e.g., `"Occupancy Report"`, `"Revenue Report"`, `"Guest Report"`)
- Or wire the report generation to `reportTypeCmb` selection
