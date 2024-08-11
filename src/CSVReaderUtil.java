import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReaderUtil {
    public static List<Account> accounts = new ArrayList<>();
    public static String filePath = "src/ATM-Database.csv";

    // Method to read accounts from CSV and clear the list first
    public static List<Account> readAccountsFromCSV(String csvFile) {
        accounts.clear(); // Clear the list before adding new data
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] nextLine;
            reader.readNext(); // Skip the header line (ID, Account, balance headers)
            while ((nextLine = reader.readNext()) != null) {
                String id = nextLine[0];
                String name = nextLine[1];
                int balance = Integer.parseInt(nextLine[2]);

                Account account = new Account(id, name, balance);
                accounts.add(account);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    // Method to update account balance
    public static void updateAccountBalance(String id, int newBalance) {
        for (Account account : accounts) {
            if (account.getId().equals(id)) {
                account.setBalance(newBalance);
                break; // Exit the loop early since we found the account
            }
        }
    }

    // Method to write accounts to CSV
    public static void writeAccountsToCSV(String filePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            // Write header
            writer.writeNext(new String[] {"ID", "Name", "Balance"});
            
            // Write each account
            for (Account account : accounts) {
                writer.writeNext(new String[] {
                    account.getId(),
                    account.getName(),
                    Integer.toString(account.getBalance())
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}