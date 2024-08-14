import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Window {
    JFrame frame;
    public static String user;
    private JButton depositBtn, withdrawBtn, balanceBtn;
    private Font font = new Font("Segoe UI Light", Font.PLAIN, 40);
    private Account account; // Instance variable to store the current account
    String filePath = CSVReaderUtil.filePath;
    List<Account> accounts;

    public Window() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        initialize();
    }

    private void initialize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // positioning window to appear in the center
        int width = 700;
        int height = 400;
        int centerX = (screenSize.width - width) / 2;
        int centerY = (screenSize.height - height) / 2;

        frame = new JFrame();
        frame.setTitle("ATM - " + user);
        frame.setBounds(centerX, centerY, 700, 290);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel activityBar = new JLabel(getSalutation() + ", " + user);
        activityBar.setFont(font);
        activityBar.setHorizontalAlignment(SwingConstants.CENTER);
        activityBar.setBounds(10, 10, 670, 75);
        frame.getContentPane().add(activityBar);

        depositBtn = createButton(depositBtn, 10, 125, 200, 80, "Deposit");
        balanceBtn = createButton(balanceBtn, 476, 125, 200, 80, "Balance");
        withdrawBtn = createButton(withdrawBtn, 242, 125, 200, 80, "Withdraw");
        
        depositBtn.addActionListener(e -> processTransaction("Deposit"));
        balanceBtn.addActionListener(e -> checkBalance());
        withdrawBtn.addActionListener(e -> processTransaction("Withdraw"));

        frame.setVisible(true);
        
        loadCSV(); // Load accounts and set the current account
    }
    
    private void processTransaction(String transactionType) {
        // Read amount from user input
        String input = JOptionPane.showInputDialog(null, "Enter amount to " + transactionType.toLowerCase() + ": ", "Input", JOptionPane.QUESTION_MESSAGE);
        
        try {
            // Convert input to integer
            int amount = Integer.parseInt(input);
            
            if(input == null) {return;}

            else if (amount <= 0) {
                // Check if the amount is zero or negative
                JOptionPane.showMessageDialog(null, "Invalid amount. Enter a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (transactionType.equals("Deposit")) {
                // Update account balance
                CSVReaderUtil.updateAccountBalance(account.getId(), account.getBalance() + amount);
                JOptionPane.showMessageDialog(null, "Deposit successful! Deposit amount: " + amount, "Success", JOptionPane.INFORMATION_MESSAGE);
                log("Deposit", amount, account);
            }
            else if (transactionType.equals("Withdraw")) {
                // Check if the amount is greater than the balance
                if (amount > account.getBalance()) {
                    JOptionPane.showMessageDialog(null, "Insufficient funds.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Update account balance
                CSVReaderUtil.updateAccountBalance(account.getId(), account.getBalance() - amount);
                JOptionPane.showMessageDialog(null, "Withdraw successful! Withdrawn amount: " + amount, "Success", JOptionPane.INFORMATION_MESSAGE);
                log("Withdraw", amount, account);
            }

            // Write to CSV and reload account data
            CSVReaderUtil.writeAccountsToCSV(filePath);
            loadCSV();
        } catch (NumberFormatException e) {
            // Handle invalid input (not an integer)
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // Handle other exceptions
            JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void log(String transaction, int amount, Account account) throws IOException {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Define the format for the timestamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        // Format the current date and time
        String formattedDateTime = currentDateTime.format(formatter);
    	String logFilePath = "src/log.txt";
    	BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true));
        
        writer.write(formattedDateTime + " | " + "User ID: " + account.getId() + " | " + "Name: " + account.getName() + " | " + transaction + " | " + "Amount: " + amount + " | " + "Balance: " + account.getBalance());
    	writer.newLine();
        writer.close(); 
    }

    private void checkBalance() {
        // Display updated balance
        JOptionPane.showMessageDialog(null, "Your account balance is " + account.getBalance(), "Balance", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void loadCSV() {
        accounts = CSVReaderUtil.readAccountsFromCSV(filePath);
        // Find the account from the list and set it
        for (Account acc : accounts) {
            if (acc.getId().equals(ScanWindow.account.getId())) {
                account = acc;
                break;
            }
        }
    }
    
    // createButton method is more efficient and saves many lines
    private JButton createButton(JButton button, int x, int y, int width, int height, String text) {
        button = new JButton("");
        button.setBounds(x, y, width, height);
        button.setFocusable(false); // no highlight border when selected with cursor
        button.setFont(font);
        button.setText(text);
        frame.getContentPane().add(button);
        
        return button;
    }
    
    private String getSalutation() {
        int currentHour = LocalTime.now().getHour(); // local system hour
        
        if (currentHour >= 6 && currentHour < 12) {
            return "Good morning"; // between 06:00 & 11:59
        } else if (currentHour >= 12 && currentHour < 18) {
            return "Good afternoon"; // between 12:00 & 17:59
        } else if (currentHour >= 18 && currentHour < 21) {
            return "Good evening"; // between 18:00 & 20:59
        } else {
            return "Hello"; // if user is active during 21:00 - 05:59
        }
    }
}