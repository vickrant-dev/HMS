package hms.util;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;

public final class IconUtil {

    private static final String ICON_PATH = "/hms/resources/icons/";

    private IconUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Loads an icon from the classpath by name and extension.
     * Falls back to a letter icon if no resource file is found.
     *
     * @param name The icon filename without extension
     * @param ext The file extension (e.g., "png", "svg")
     * @return The loaded ImageIcon, or a fallback letter icon if not found
     */
    public static ImageIcon loadIcon(String name, String ext) {
        URL resource = IconUtil.class.getResource(ICON_PATH + name + "." + ext);
        if (resource != null) {
            return new ImageIcon(resource);
        }
        if (name == null || name.isBlank()) {
            return null;
        }
        return createLetterIcon(name.substring(0, 1).toUpperCase());
    }

    /**
     * Loads a PNG icon from the classpath by name.
     * Falls back to a letter icon if no resource file is found.
     */
    public static ImageIcon loadIcon(String name) {
        return loadIcon(name, "png");
    }

    /** Returns the add icon. */
    public static ImageIcon getAddIcon() {
        return loadIcon("add");
    }

    /** Returns the edit icon. */
    public static ImageIcon getEditIcon() {
        return loadIcon("edit");
    }

    /** Returns the delete icon. */
    public static ImageIcon getDeleteIcon() {
        return loadIcon("delete");
    }

    /** Returns the save icon. */
    public static ImageIcon getSaveIcon() {
        return loadIcon("save");
    }

    /** Returns the search icon. */
    public static ImageIcon getSearchIcon() {
        return loadIcon("search");
    }

    /** Returns the refresh icon. */
    public static ImageIcon getRefreshIcon() {
        return loadIcon("refresh");
    }

    /** Returns the print icon. */
    public static ImageIcon getPrintIcon() {
        return loadIcon("print");
    }

    public static ImageIcon getBookIcon() {
        return loadIcon("book");
    }

    public static ImageIcon getCheckIcon() {
        return loadIcon("check");
    }

    public static ImageIcon getEyeIcon() {
        return loadIcon("eye");
    }

    public static ImageIcon getInvoiceIcon() {
        return loadIcon("invoice");
    }

    public static ImageIcon getWrenchIcon() {
        return loadIcon("wrench");
    }

    /**
     * Creates a simple letter icon as a fallback when no image file is available.
     */
    private static ImageIcon createLetterIcon(String text) {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(UIManager.getColor("Button.foreground"));
        g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (16 - fm.stringWidth(text)) / 2;
        int y = ((16 - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(text, x, y);
        g2d.dispose();
        return new ImageIcon(img);
    }
}
