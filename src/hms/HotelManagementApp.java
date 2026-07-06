package hms;

import com.formdev.flatlaf.FlatDarkLaf;
import hms.view.MainWindow;
import javax.swing.SwingUtilities;
import java.io.InputStream;

public class HotelManagementApp {

    public static void main(String[] args) {
        try {
            
            java.util.Properties props = new java.util.Properties();
            try (InputStream themeStream = HotelManagementApp.class.getResourceAsStream("/hms/theme/CustomTheme.properties")) {
                if (themeStream != null) {
                    props.load(themeStream);
                } else {
                    System.err.println("Theme file not found!");
                }
            }

            // Convert Map<Object, Object> to Map<String, String>
            java.util.Map<String, String> customProps = new java.util.HashMap<>();
            props.forEach((key, value) -> customProps.put(String.valueOf(key), String.valueOf(value)));

            // Now pass the correctly typed map
            com.formdev.flatlaf.FlatLaf.setGlobalExtraDefaults(customProps);
            com.formdev.flatlaf.FlatDarkLaf.setup();
            
//            com.formdev.flatlaf.themes.FlatMacDarkLaf.setup();
        }
        catch (Exception e) {
            System.out.println("Error occured loading flatlaf theme: " + e.getMessage());
        }

        // runs on Event Dispatch Thread (EDT) instead of main thread.
        // prevents random glitches/race conditions.
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });
    }
}
