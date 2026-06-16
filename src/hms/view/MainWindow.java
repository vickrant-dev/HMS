package hms.view;

import hms.config.Constants;
import javax.swing.JFrame;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle(Constants.APP_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setLocationRelativeTo(null);
    }
}
