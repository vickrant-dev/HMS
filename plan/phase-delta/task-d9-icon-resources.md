# Task D9 — Icon Resource Files

**Area:** Phase 4.5 — IconUtil (completion)  
**Severity:** LOW — letter fallback icons work but look unprofessional

## Problem

`IconUtil` at `src/hms/util/IconUtil.java` looks for icon files at `/hms/resources/icons/` but:

1. The directory `src/hms/resources/icons/` **does not exist**
2. No `.png` or other image files exist
3. All `loadIcon()` calls return fallback letter icons generated in `createLetterIcon()` (e.g., "A" for add)

## Changes Required

### Option A: Add actual icon PNG files (recommended)

Create resource files at `src/hms/resources/icons/`:

| File | Size | Description |
|------|------|-------------|
| `add.png` | 16×16 | Plus sign icon |
| `edit.png` | 16×16 | Pencil icon |
| `delete.png` | 16×16 | Trash/X icon |
| `save.png` | 16×16 | Floppy disk icon |
| `search.png` | 16×16 | Magnifying glass icon |
| `refresh.png` | 16×16 | Circular arrow icon |
| `print.png` | 16×16 | Printer icon |

**Sources:**
- Free icon set (e.g., Material Design Icons, FontAwesome, Feather Icons)
- Or generate programmatically if licenses are a concern

### Option B: Improve fallback icons

Enhance `createLetterIcon()` to produce more visually appealing fallbacks:
- Use Unicode symbols instead of letters (✚ for add, ✏ for edit, 🗑 for delete, 💾 for save, 🔍 for search, 🔄 for refresh, 🖨 for print)
- Enlarge to 24×24 for better visibility
- Add colored backgrounds per action type

### Option C: Both A + B

Use PNG files as primary, letter/emoji fallback when files are missing (current behavior).

## Verification

1. All 7 named icon methods return non-null `ImageIcon`
2. Icons display correctly on buttons in all panels
3. Fallback icons (if any) are visually acceptable

## Checklist

- [ ] Create `src/hms/resources/icons/` directory
- [ ] Add PNG icon files for all 7 named icons
- [ ] (Optional) Enhance fallback icon generation
- [ ] Verify Clean & Build
