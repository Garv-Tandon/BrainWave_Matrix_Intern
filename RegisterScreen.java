import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterScreen extends JFrame implements ActionListener {

    private JTextField accField, nameField;
    private JPasswordField pinField;
    private JButton registerButton, backButton;

    // DB Details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/atm_db?useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Garv@2004";

    public RegisterScreen() {

        setTitle("Create Account");
        setSize(480, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel bgPanel = new JPanel();
        bgPanel.setLayout(null);
        bgPanel.setBackground(new Color(40, 55, 71));
        add(bgPanel);

        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(Color.WHITE);
        card.setBounds(80, 40, 320, 320);
        card.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        bgPanel.add(card);

        JLabel title = new JLabel("REGISTER", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBounds(0, 10, 320, 40);
        card.add(title);

        accField = new JTextField();
        accField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        accField.setBorder(BorderFactory.createTitledBorder("Account Number"));
        accField.setBounds(45, 70, 230, 45);
        card.add(accField);

        nameField = new JTextField();
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        nameField.setBorder(BorderFactory.createTitledBorder("Full Name"));
        nameField.setBounds(45, 125, 230, 45);
        card.add(nameField);

        pinField = new JPasswordField();
        pinField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        pinField.setBorder(BorderFactory.createTitledBorder("PIN"));
        pinField.setBounds(45, 180, 230, 45);
        card.add(pinField);

        registerButton = new JButton("Register");
        styleBtn(registerButton, new Color(39, 174, 96));
        registerButton.setBounds(45, 240, 110, 40);
        card.add(registerButton);

        backButton = new JButton("Back");
        styleBtn(backButton, new Color(52, 152, 219));
        backButton.setBounds(165, 240, 110, 40);
        card.add(backButton);

        registerButton.addActionListener(this);
        backButton.addActionListener(this);

        setVisible(true);
    }

    private void styleBtn(JButton btn, Color bgColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private boolean insertAccount(String acc, String name, String pin) {

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO accounts(account_number, name, pin, balance) VALUES(?,?,?,0)")) {

            stmt.setString(1, acc);
            stmt.setString(2, name);
            stmt.setString(3, pin);

            return stmt.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "❌ Account Already Exists!");
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == registerButton) {

            String acc = accField.getText();
            String name = nameField.getText();
            String pin = new String(pinField.getPassword());

            if (acc.isEmpty() || name.isEmpty() || pin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠ All fields are required!");
                return;
            }

            if (insertAccount(acc, name, pin)) {
                JOptionPane.showMessageDialog(this, "✔ Account Created Successfully!");
                this.dispose();
                new LoginScreen();
            }

        } else if (e.getSource() == backButton) {
            this.dispose();
            new LoginScreen();
        }
    }
}
