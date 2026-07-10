# Task D6 — Reports Module (Phase 14)

**Area:** Phase 14 — Reporting Module (full implementation)  
**Severity:** HIGH — Reports tab has zero functionality, `ReportUtil` will throw at runtime

## Problem

The Reports tab exists with a UI shell but no functionality:

| Component | Status |
|-----------|--------|
| `ReportsPanel.java` | ✅ UI shell exists, both button handlers are `// TODO` stubs |
| `.jrxml` or `.jasper` templates | ❌ **None exist anywhere in project** |
| `src/hms/reports/` directory | ❌ **Does not exist** |
| `ReportUtil.loadReport()` | ❌ Will throw `JRException("Report not found")` at runtime |

## Changes Required

### 1. Create report templates

Design 2–3 JasperReports `.jrxml` files covering:
- **Occupancy & Revenue Report** — room occupancy % by type, revenue by room type, trends by month, filterable by date range + room type
- **Guest Invoice / Folio Report** — multi-table: guest info, reservation details, room, billing itemization (room charges, service charges, taxes, discounts, late fees, total), services booked

Place compiled `.jasper` files in: `src/hms/reports/`

### 2. Create the reports directory

```
src/hms/reports/
  occupancy_report.jasper
  invoice_report.jasper
```

### 3. Wire `ReportsPanel`

- `generateReportBtnActionPerformed()` (~line 225):
  - Read filter controls (date range, room type, status combo boxes)
  - Build parameters `Map<String, Object>`
  - Call `ReportUtil.fillReport(report, parameters)`
  - Display result in preview area

- `exportPdfBtnActionPerformed()` (~line 229):
  - Call `ReportUtil.exportToPdf(jasperPrint, outputPath)`
  - Show save dialog to pick output path

### 4. (Optional) Refactor `ReportsPanel` to extend or use a dedicated report controller

### 5. Update `Constants.java` if needed

Verify `JASPER_REPORT_PATH` and define `REPORT_OUTPUT_DIR` if missing.

## Dependencies

- JasperReports 7.0.6 JAR must be on classpath (already set up in Phase 0.2)
- SQL queries in `.jrxml` must match the `hotel_management_system` schema
- Report templates must be compiled with `JasperCompileManager.compileReport()` before runtime

## Verification

1. Open Reports tab → both filters and preview area are visible
2. Select Occupancy report, set date range, click Generate → preview shows real data
3. Click Export PDF → file saves correctly
4. Switch to Invoice report → generate → preview shows real data
5. PDF exports open correctly in external viewer

## Checklist

- [ ] Design and write `occupancy_report.jrxml`
- [ ] Design and write `invoice_report.jrxml`
- [ ] Compile both to `.jasper` in `src/hms/reports/`
- [ ] Create `src/hms/reports/` directory
- [ ] Wire `generateReportBtnActionPerformed()`
- [ ] Wire `exportPdfBtnActionPerformed()`
- [ ] Verify `ReportUtil.loadReport()` loads correctly
- [ ] Verify Clean & Build
