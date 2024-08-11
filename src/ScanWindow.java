import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.opencsv.CSVReader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

public class ScanWindow {
    private JFrame frame;
    private SerialCommHandler serialHandler = new SerialCommHandler("COM4", 57600);
    public static Account account;
    public static List<Account> accounts = CSVReaderUtil.readAccountsFromCSV(CSVReaderUtil.filePath);

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	ScanWindow window = new ScanWindow();
                    window.frame.setVisible(true);
                    window.frame.setTitle("ATM Login");
                    window.frame.setResizable(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ScanWindow() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        initialize();
    }

    private void initialize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        int width = 365;
        int height = 175;
        int centerX = (screenSize.width - width) / 2;
        int centerY = (screenSize.height - height) / 2;
        
        frame = new JFrame();
        frame.setBounds(centerX, centerY, width, height);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JButton proceedButton = new JButton("Scan Card");
        proceedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(serialHandler.openPort()) {
            		String data = serialHandler.readData();
            		System.out.println(CSVReaderUtil.accounts);
            		for(Account account : CSVReaderUtil.accounts) {
            			if(account.getId().equals(data)) {
            				ScanWindow.account = account;
                			Window.user = account.getName();
                            openSecondFrame();
            			}
            		}
            	}
            	else {
            		//title.setText("Failed to open port");
            	}
            }
        });
        proceedButton.setFont(new Font("Segoe UI Light", Font.PLAIN, 45));
        proceedButton.setBounds(10, 35, 330, 80);
        proceedButton.setFocusable(false);
        frame.getContentPane().add(proceedButton);
    }

    private void openSecondFrame() {
        // Close the current frame
        frame.dispose();

        // Open the second frame
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Window(); // This creates and shows the second frame
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}