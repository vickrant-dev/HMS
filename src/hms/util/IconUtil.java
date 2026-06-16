package hms.util;

import javax.swing.ImageIcon;
import java.net.URL;

public final class IconUtil {

    private static final String ICON_PATH = "/hms/resources/icons/";

    private IconUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static ImageIcon loadIcon(String name, String ext) {
        URL resource = IconUtil.class.getResource(ICON_PATH + name + "." + ext);
        if (resource == null) {
            return null;
        }
        return new ImageIcon(resource);
    }

    public static ImageIcon loadIcon(String name) {
        return loadIcon(name, "png");
    }

    public static ImageIcon getAddIcon() {
        return loadIcon("add");
    }

    public static ImageIcon getEditIcon() {
        return loadIcon("edit");
    }

    public static ImageIcon getDeleteIcon() {
        return loadIcon("delete");
    }

    public static ImageIcon getSaveIcon() {
        return loadIcon("save");
    }

    public static ImageIcon getSearchIcon() {
        return loadIcon("search");
    }

    public static ImageIcon getRefreshIcon() {
        return loadIcon("refresh");
    }

    public static ImageIcon getPrintIcon() {
        return loadIcon("print");
    }
}
