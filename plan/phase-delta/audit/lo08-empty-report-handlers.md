# LO08 — ReportsPanel Handler Bodies Empty

**Severity:** LOW  
**File:** `src/hms/view/panels/ReportsPanel.java`

## Problem

Both action handlers have empty or placeholder bodies:
- `generateReportBtnActionPerformed` — `// TODO add your handling code here:`
- `exportPdfBtnActionPerformed` — `// TODO add your handling code here:`

The `reportTypeCmb` also has placeholder values (`"Item 1"`–`"Item 4"`). No report generation or export logic is wired despite the D6 reports module being implemented.

**Impact:** Reports feature is non-functional.

## Fix

Wire the handlers to:
1. Read selected report type and filter criteria
2. Call `ReportCompiler` to generate the report
3. Display in `reportPreviewPanel` (JRViewer)
4. Wire `exportPdfBtn` to PDF export
