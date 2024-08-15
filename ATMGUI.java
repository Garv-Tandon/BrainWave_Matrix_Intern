import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ATMGUI extends JFrame implements ActionListener {
    private double balance;
    private JLabel balanceLabel;
    private JTextField amountField;
    private JButton checkBalanceButton, depositButton, withdrawButton,transferButton, clearButton,exitButton;

    public ATMGUI(double initialBalance) {
        this.balance = initialBalance;

        // Frame settings
        setTitle("ATM Interface");
        setSize(400, 300);
        setLayout(new GridLayout(5, 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Setting for Cursor
        Cursor c1 = new Cursor(Cursor.HAND_CURSOR); 
        Cursor c2 = new Cursor(Cursor.HAND_CURSOR); 
        Cursor c3 = new Cursor(Cursor.HAND_CURSOR); 
        Cursor c4 = new Cursor(Cursor.HAND_CURSOR); 
        Cursor c5 = new Cursor(Cursor.HAND_CURSOR); 
        Cursor c6 = new Cursor(Cursor.HAND_CURSOR); 


        // Components
        balanceLabel = new JLabel("");
        balanceLabel.setFont(new Font("Arial",Font.PLAIN,28));
        updateBalanceLabel();

        JLabel amountLabel = new JLabel("Enter amount:");
        amountField = new JTextField();
        amountField.setFont(new Font("Arial",Font.PLAIN,28));
        amountLabel.setFont(new Font("Arial",Font.PLAIN,28));
        checkBalanceButton = new JButton("Check Balance");
        checkBalanceButton.setFont(new Font("Arial",Font.PLAIN,28));
        checkBalanceButton.setBackground(Color.RED);
        checkBalanceButton.setForeground(Color.WHITE);
        depositButton = new JButton("Deposit");
        depositButton.setFont(new Font("Arial",Font.PLAIN,28));
        depositButton.setBackground(Color.RED);
        depositButton.setForeground(Color.WHITE);
        withdrawButton = new JButton("Withdraw");
        withdrawButton.setFont(new Font("Arial",Font.PLAIN,28));
        withdrawButton.setBackground(Color.RED);
        withdrawButton.setForeground(Color.WHITE);
        transferButton=new JButton("Transfer Money");
        transferButton.setFont(new Font("Arial",Font.PLAIN,28));
        transferButton.setBackground(Color.RED);
        transferButton.setForeground(Color.WHITE);
        clearButton=new JButton("X");
        clearButton.setBackground(Color.BLACK);
        clearButton.setForeground(Color.WHITE);
        clearButton.setFont(new Font("Arial",Font.PLAIN,28));
        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial",Font.PLAIN,28));
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.WHITE);
        

        // Add action listeners
        checkBalanceButton.addActionListener(this);
        depositButton.addActionListener(this);
        withdrawButton.addActionListener(this);
        transferButton.addActionListener(this);
        clearButton.addActionListener(this);
        exitButton.addActionListener(this);

        //Add Cursor to buttons
        checkBalanceButton.setCursor(c1);
        depositButton.setCursor(c2);
        withdrawButton.setCursor(c3);
        transferButton.setCursor(c4);
        exitButton.setCursor(c5);
        clearButton.setCursor(c6);

        // Add components to frame
        add(balanceLabel);
        add(new JLabel("")); // Empty label for spacing
        add(amountLabel);
        add(amountField);
        add(checkBalanceButton);
        add(depositButton);
        add(withdrawButton);
        add(transferButton);
        add(clearButton);
        add(exitButton);

        
        

        // Set frame visibility
        setVisible(true);
    }

    private void updateBalanceLabel() {
        balanceLabel.setText("");
    }

    public void actionPerformed(ActionEvent e){

        

        String command = e.getActionCommand();

        switch (command) {
            case "Check Balance":
        
                updateBalanceLabel();
                JOptionPane.showMessageDialog(this, "Your balance is Rs" + String.format("%.2f", balance));
                break;
    

            case "Deposit":
                try {
                    double depositAmount = Double.parseDouble(amountField.getText());
                    if (depositAmount > 0) {
                        balance += depositAmount;
                        updateBalanceLabel();
                        JOptionPane.showMessageDialog(this, "Deposited Rs" + String.format("%.2f", depositAmount));
                    } else {
                        JOptionPane.showMessageDialog(this, "Deposit amount must be positive.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid amount entered.");
                }
                break;

            case "Withdraw":
                try {
                    double withdrawAmount = Double.parseDouble(amountField.getText());
                    if (withdrawAmount > 0) {
                        if (withdrawAmount <= balance) {
                            balance -= withdrawAmount;
                            updateBalanceLabel();
                            JOptionPane.showMessageDialog(this, "Withdrew Rs" + String.format("%.2f", withdrawAmount));
                        } else {
                            JOptionPane.showMessageDialog(this, "Insufficient funds.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Withdrawal amount must be positive.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid amount entered.");
                }
                break;
            case "Transfer Money":
                                 JFrame f1=new JFrame("");
                                 String accNo=JOptionPane.showInputDialog(f1,"Enter the acc no");
                                 String amt=JOptionPane.showInputDialog(f1,"Enter the amount");
                                 int a=Integer.parseInt(amt);
                            if(a<=balance)
                            {
                                  if(a<=25000)

                                  {
                                   balance-=a;
                                   updateBalanceLabel();
                                   JOptionPane.showMessageDialog(this, "Transfer Rs" + String.format("%.2f", a));
                                  }
                                 else
                                  {
                                  JOptionPane.showMessageDialog(this, "The limit is exceeded");
                                  }
                            }
                         else
                           {
                            JOptionPane.showMessageDialog(this, "Invalid Amount");
                           }
                         break;
            case "X":
                    amountField.setText(" ");
                    break;


            case "Exit":
                JOptionPane.showMessageDialog(this, "Thank you for using the ATM. Goodbye!");
                System.exit(0);
                break;

            default:
                JOptionPane.showMessageDialog(this, "Unknown command.");
        }
    
        amountField.setText(""); // Clear the input field after each transaction
    
    
    }

    public static void main(String[] args) {
        String userPin = JOptionPane.showInputDialog("Enter your ATM PIN:");     
        if(userPin.equals("1234"))
        {

        SwingUtilities.invokeLater(() -> new ATMGUI(1000));
        }
        else
        {
            System.out.println("Wrong Pin");
            main(args);
        }
    }
}
