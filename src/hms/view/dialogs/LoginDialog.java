package hms.view.dialogs;

import hms.controller.StaffController;
import hms.exception.DatabaseException;
import hms.model.Staff;
import hms.util.IconUtil;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class LoginDialog extends JDialog {

    private final StaffController staffController = new StaffController();
    private Staff authenticatedStaff;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel errorLabel;

    public LoginDialog() {
        super((java.awt.Frame) null, "Login", true);
        initUI();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(420, 440);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initUI() {
        java.awt.Color bg = UIManager.getColor("Panel.background");
        java.awt.Color fg = UIManager.getColor("Label.foreground");

        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(bg);

        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(bg);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        cardPanel.setPreferredSize(new Dimension(360, 360));

        java.awt.Color accentColor = UIManager.getColor("Component.borderColor");
        if (accentColor == null) accentColor = new java.awt.Color(70, 130, 180);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel appIconLabel = new JLabel();
        appIconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        java.awt.Image appImage = IconUtil.getAppIcon();
        if (appImage != null) {
            appIconLabel.setIcon(new ImageIcon(appImage.getScaledInstance(48, 48, java.awt.Image.SCALE_SMOOTH)));
        }
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 8, 0);
        cardPanel.add(appIconLabel, gbc);

        JLabel titleLabel = new JLabel("Hotel Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(fg);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 4, 0);
        cardPanel.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("Sign in with your staff account", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(fg);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 24, 0);
        cardPanel.add(subtitleLabel, gbc);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        emailLabel.setForeground(fg);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 4, 0);
        gbc.anchor = GridBagConstraints.WEST;
        cardPanel.add(emailLabel, gbc);

        emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(280, 36));
        emailField.setMinimumSize(new Dimension(280, 36));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 14, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        cardPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passwordLabel.setForeground(fg);
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 4, 0);
        gbc.anchor = GridBagConstraints.WEST;
        cardPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(280, 36));
        passwordField.setMinimumSize(new Dimension(280, 36));
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        cardPanel.add(passwordField, gbc);

        errorLabel = new JLabel(" ", SwingConstants.CENTER);
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(new java.awt.Color(220, 50, 50));
        errorLabel.setPreferredSize(new Dimension(280, 18));
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 8, 0);
        cardPanel.add(errorLabel, gbc);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(bg);
        GridBagConstraints btnGbc = new GridBagConstraints();
        btnGbc.fill = GridBagConstraints.HORIZONTAL;
        btnGbc.insets = new Insets(0, 4, 0, 4);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(130, 36));
        loginButton.setBackground(accentColor);
        loginButton.setForeground(java.awt.Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> doLogin());
        btnGbc.gridx = 0;
        buttonPanel.add(loginButton, btnGbc);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cancelButton.setPreferredSize(new Dimension(130, 36));
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> System.exit(0));
        btnGbc.gridx = 1;
        buttonPanel.add(cancelButton, btnGbc);

        gbc.gridy = 8;
        gbc.insets = new Insets(4, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        cardPanel.add(buttonPanel, gbc);

        outerPanel.add(cardPanel);
        add(outerPanel);

        getRootPane().setDefaultButton(loginButton);
        passwordField.addActionListener(e -> doLogin());
        emailField.addActionListener(e -> passwordField.requestFocusInWindow());
    }

    private void doLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter email and password");
            return;
        }

        try {
            Staff staff = staffController.authenticate(email, password);
            if (staff != null) {
                authenticatedStaff = staff;
                dispose();
            } else {
                errorLabel.setText("Invalid email or password");
                passwordField.setText("");
                passwordField.requestFocusInWindow();
            }
        } catch (DatabaseException e) {
            errorLabel.setText("Database error: " + e.getMessage());
        }
    }

    public Staff getAuthenticatedStaff() {
        return authenticatedStaff;
    }
}
