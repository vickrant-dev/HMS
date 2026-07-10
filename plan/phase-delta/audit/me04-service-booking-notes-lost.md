# ME04 — ServiceBookingDialog Notes Never Saved

**Severity:** MEDIUM  
**File:** `src/hms/view/dialogs/ServiceBookingDialog.java`

## Problem

The dialog has a notes text area (`jTextArea1`), but its content is never read or passed to `serviceController.createServiceBooking()`.

**Impact:** Service booking notes are lost.

## Fix

Read notes from `jTextArea1.getText()` and pass to `createServiceBooking()`. The model `ServiceBooking` has no `notes` field currently — either add one or store in a separate field.
