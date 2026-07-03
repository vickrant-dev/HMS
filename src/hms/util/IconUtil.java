package hms.util;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;

public final class IconUtil {

    private static final String ICON_PATH = "/hms/resources/icons/";

    private IconUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Loads an SVG icon from the classpath by name.
     * Falls back to a letter icon if no resource file is found.
     */
    public static Icon loadIcon(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        URL svgResource = IconUtil.class.getResource(ICON_PATH + name + ".svg");
        if (svgResource != null) {
            return new FlatSVGIcon(svgResource);
        }
        return createLetterIcon(name.substring(0, 1).toUpperCase());
    }

    /**
     * Loads an icon from the classpath by name and extension.
     * Falls back to a letter icon if no resource file is found.
     */
    public static Icon loadIcon(String name, String ext) {
        URL resource = IconUtil.class.getResource(ICON_PATH + name + "." + ext);
        if (resource != null) {
            if ("svg".equalsIgnoreCase(ext)) {
                return new FlatSVGIcon(resource);
            }
            return new ImageIcon(resource);
        }
        if (name == null || name.isBlank()) {
            return null;
        }
        return createLetterIcon(name.substring(0, 1).toUpperCase());
    }

    public static Icon getAddIcon() {
        return loadIcon("add");
    }

    public static Icon getEditIcon() {
        return loadIcon("edit");
    }

    public static Icon getDeleteIcon() {
        return loadIcon("delete");
    }

    public static Icon getSaveIcon() {
        return loadIcon("save");
    }

    public static Icon getSearchIcon() {
        return loadIcon("search");
    }

    public static Icon getRefreshIcon() {
        return loadIcon("refresh");
    }

    public static Icon getPrintIcon() {
        return loadIcon("print");
    }

    public static Icon getBookIcon() {
        return loadIcon("book");
    }

    public static Icon getCheckIcon() {
        return loadIcon("check");
    }

    public static Icon getEyeIcon() {
        return loadIcon("eye");
    }

    public static Icon getInvoiceIcon() {
        return loadIcon("invoice");
    }

    public static Icon getWrenchIcon() {
        return loadIcon("wrench");
    }

    /**
     * Loads the app window icon from an SVG file.
     * Renders the SVG to a BufferedImage for use with setIconImage().
     */
    public static Image getAppIcon() {
        URL svgResource = IconUtil.class.getResource(ICON_PATH + "app.svg");
        if (svgResource == null) {
            return null;
        }
        FlatSVGIcon svgIcon = new FlatSVGIcon(svgResource);
        BufferedImage image = new BufferedImage(
            svgIcon.getIconWidth(), svgIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        svgIcon.paintIcon(null, g2d, 0, 0);
        g2d.dispose();
        return image;
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
