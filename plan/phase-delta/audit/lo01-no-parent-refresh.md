# LO01 — All Dialogs Fail to Refresh Parent Panel After Save

**Severity:** LOW  
**Affected:** All 12 dialog files in `src/hms/view/dialogs/`

## Problem

Every dialog's save/create/update/delete handler ends with only `dispose()`. None of them invoke a callback or fire an event to refresh the parent panel's table data. After a successful operation, the user must manually navigate away and back to see the changes.

**Impact:** Poor UX — user must manually refresh.

## Fix

Add a `Runnable` callback parameter to each dialog constructor that the panel provides to trigger a data reload after a successful operation.
