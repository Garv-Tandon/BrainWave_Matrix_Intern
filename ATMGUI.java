import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ATMGUI extends JFrame implements ActionListener {

    private String accountNumber;
    private JLabel welcome;

    private JButton checkBalBtn, depositBtn, withdrawBtn, transferBtn,
            changePinBtn, miniStmtBtn, deleteAccBtn, logoutBtn;

    // DB Details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/atm_db?useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Garv@2004";

    public ATMGUI(String accNo) {

        this.accountNumber = accNo;

        // Window Settings
        setTitle("ATM Dashboard");
        setSize(600, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(40, 55, 71));
        add(panel);

        // Welcome Header
        welcome = new JLabel("Welcome, Account: " + accountNumber);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 26));
        welcome.setForeground(Color.WHITE);
        welcome.setBounds(80, 20, 500, 50);
        panel.add(welcome);

        // Buttons
        checkBalBtn = createButton("Check Balance", 90);
        depositBtn   = createButton("Deposit", 150);
        withdrawBtn  = createButton("Withdraw", 210);
        transferBtn  = createButton("Transfer Amount", 270);
        changePinBtn = createButton("Change PIN", 330);
        miniStmtBtn  = createButton("Mini Statement", 390);
        deleteAccBtn = createButton("Delete Account", 450);
        logoutBtn    = createButton("Logout", 520);

        panel.add(checkBalBtn);
        panel.add(depositBtn);
        panel.add(withdrawBtn);
        panel.add(transferBtn);
        panel.add(changePinBtn);
        panel.add(miniStmtBtn);
        panel.add(deleteAccBtn);
        panel.add(logoutBtn);

        checkBalBtn.addActionListener(this);
        depositBtn.addActionListener(this);
        withdrawBtn.addActionListener(this);
        transferBtn.addActionListener(this);
        changePinBtn.addActionListener(this);
        miniStmtBtn.addActionListener(this);
        deleteAccBtn.addActionListener(this);
        logoutBtn.addActionListener(this);

        setVisible(true);
    }

    private JButton createButton(String text, int y) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(34, 153, 84));
        btn.setBounds(150, y, 300, 50);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Fetch current balance
    private double getBalance() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT balance FROM accounts WHERE account_number=?")) {

            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return rs.getDouble("balance");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
        }
        return -1;
    }

    // Update balance
    private boolean updateBalance(double newBalance) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE accounts SET balance=? WHERE account_number=?")) {

            stmt.setDouble(1, newBalance);
            stmt.setString(2, accountNumber);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // 1️⃣ Check Balance
        if (e.getSource() == checkBalBtn) {
            double bal = getBalance();
            JOptionPane.showMessageDialog(this,
                    "Your Current Balance: ₹" + bal,
                    "Balance Status",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        // 2️⃣ Deposit
        else if (e.getSource() == depositBtn) {
            String amtStr = JOptionPane.showInputDialog(this, "Enter Deposit Amount:");
            if (amtStr != null && !amtStr.isEmpty()) {
                double amt = Double.parseDouble(amtStr);
                updateBalance(getBalance() + amt);
                JOptionPane.showMessageDialog(this, "Deposit Successful!");
            }
        }

        // 3️⃣ Withdraw
        else if (e.getSource() == withdrawBtn) {
            String amtStr = JOptionPane.showInputDialog(this, "Enter Withdrawal Amount:");
            if (amtStr != null && !amtStr.isEmpty()) {
                double amt = Double.parseDouble(amtStr);
                double current = getBalance();

                if (amt > current) {
                    JOptionPane.showMessageDialog(this, "Insufficient Balance!");
                } else {
                    updateBalance(current - amt);
                    JOptionPane.showMessageDialog(this, "Withdrawal Successful!");
                }
            }
        }

        // 4️⃣ Transfer Money
        else if (e.getSource() == transferBtn) {

            String targetAcc = JOptionPane.showInputDialog(this, "Enter Receiver Account Number:");
            String amtStr = JOptionPane.showInputDialog(this, "Enter Amount to Transfer:");

            if (targetAcc != null && amtStr != null) {

                try {
                    double amt = Double.parseDouble(amtStr);
                    double current = getBalance();

                    if (amt > current) {
                        JOptionPane.showMessageDialog(this, "Insufficient Balance!");
                        return;
                    }

                    updateBalance(current - amt);

                    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                         PreparedStatement stmt = conn.prepareStatement(
                                 "UPDATE accounts SET balance = balance + ? WHERE account_number=?")) {

                        stmt.setDouble(1, amt);
                        stmt.setString(2, targetAcc);
                        stmt.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(this, "Transfer Successful!");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Transfer Error: " + ex.getMessage());
                }
            }
        }

        // 5️⃣ Change PIN
        else if (e.getSource() == changePinBtn) {
            String newPin = JOptionPane.showInputDialog(this, "Enter New PIN:");
            if (newPin != null && !newPin.isEmpty()) {
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                     PreparedStatement stmt = conn.prepareStatement(
                             "UPDATE accounts SET pin=? WHERE account_number=?")) {

                    stmt.setString(1, newPin);
                    stmt.setString(2, accountNumber);
                    stmt.executeUpdate();

                    JOptionPane.showMessageDialog(this, "PIN Updated Successfully!");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
                }
            }
        }

        // 6️⃣ Mini Statement (Future Feature)
        else if (e.getSource() == miniStmtBtn) {
            JOptionPane.showMessageDialog(this,
                    "Mini Statement Feature Coming Soon (Requires transaction history table).");
        }

        // 7️⃣ Delete Account
        else if (e.getSource() == deleteAccBtn) {

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete your account?\nThis cannot be undone!",
                    "WARNING", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {

                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                     PreparedStatement stmt = conn.prepareStatement(
                             "DELETE FROM accounts WHERE account_number=?")) {

                    stmt.setString(1, accountNumber);
                    stmt.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Account Deleted Successfully!");

                    this.dispose();
                    new LoginScreen();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
                }
            }
        }

        // 8️⃣ Logout
        else if (e.getSource() == logoutBtn) {
            JOptionPane.showMessageDialog(this, "Logged Out Successfully!");
            this.dispose();
            new LoginScreen();
        }
    }
}
