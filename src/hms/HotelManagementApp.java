package hms;

import hms.view.MainWindow;
import javax.swing.SwingUtilities;

public class HotelManagementApp {

    public static void main(String[] args) {
        try {
            com.formdev.flatlaf.themes.FlatMacDarkLaf.setup();
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
