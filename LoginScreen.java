import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginScreen extends JFrame implements ActionListener {

    private JTextField accountField;
    private JPasswordField pinField;
    private JButton loginButton, registerButton;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/atm_db?useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Garv@2004";

    public LoginScreen() {

        setTitle("ATM Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Background Panel
        JPanel bgPanel = new JPanel();
        bgPanel.setLayout(null);
        bgPanel.setBackground(new Color(40, 55, 71));
        add(bgPanel);

        // Glass transparent card
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(new Color(236, 240, 241));
        card.setBounds(75, 40, 300, 240);
        card.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        bgPanel.add(card);

        // Title
        JLabel title = new JLabel("ATM LOGIN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBounds(0, 10, 300, 40);
        card.add(title);

        // Account field
        accountField = new JTextField();
        accountField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        accountField.setBorder(BorderFactory.createTitledBorder("Account Number"));
        accountField.setBounds(40, 65, 220, 45);
        card.add(accountField);

        // Pin field
        pinField = new JPasswordField();
        pinField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        pinField.setBorder(BorderFactory.createTitledBorder("PIN"));
        pinField.setBounds(40, 115, 220, 45);
        card.add(pinField);

        // Login Button
        loginButton = new JButton("Login");
        decorateButton(loginButton, new Color(39, 174, 96));
        loginButton.setBounds(35, 175, 110, 40);
        card.add(loginButton);

        // Register Button
        registerButton = new JButton("Register");
        decorateButton(registerButton, new Color(52, 152, 219));
        registerButton.setBounds(155, 175, 110, 40);
        card.add(registerButton);

        loginButton.addActionListener(this);
        registerButton.addActionListener(this);

        setVisible(true);
    }

    // Custom Button Styling
    private void decorateButton(JButton btn, Color bg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private boolean validateLogin(String acc, String pin) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM accounts WHERE account_number=? AND pin=?")) {

            stmt.setString(1, acc);
            stmt.setString(2, pin);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == loginButton) {

            String acc = accountField.getText();
            String pin = new String(pinField.getPassword());

            if (validateLogin(acc, pin)) {
                this.dispose();
                new ATMGUI(acc);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Account Number or PIN");
            }

        } else if (e.getSource() == registerButton) {
            this.dispose();
            new RegisterScreen();
        }
    }
}
