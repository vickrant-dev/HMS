# Task 4.7 — ReportUtil.java

**Package:** `hms.util`
**File:** `src/hms/util/ReportUtil.java`

## Description
JasperReports helper utility: load compiled `.jasper` reports, fill with data, export to PDF.

## Methods

| # | Method | Returns | Description |
|---|--------|---------|-------------|
| 1 | `loadReport(String)` | `JasperReport` | Load from `JASPER_REPORT_PATH + name + ".jasper"` |
| 2 | `fillReport(JasperReport, Map)` | `JasperPrint` | Fill using DB connection |
| 3 | `fillReport(JasperReport, Map, JRDataSource)` | `JasperPrint` | Fill using custom data source |
| 4 | `exportToPdf(JasperPrint, String)` | `void` | Export to PDF file |
| 5 | `generatePdfReport(String, Map, String)` | `void` | Load → fill → export one-shot |

## Dependencies
- JasperReports 7.0.6 (already in lib/ + classpath)
- DatabaseConnection (for connection-based fills)
- Constants.JASPER_REPORT_PATH

## Checklist
- [x] Create checklist file
- [x] Write `ReportUtil.java`
- [x] Verify Clean & Build
