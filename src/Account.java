public class Account {
    private String id;
    private String name;
    private int balance;

    // Constructor
    public Account(String id, String name, int balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    // Getters and setters
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + balance;
    }
}