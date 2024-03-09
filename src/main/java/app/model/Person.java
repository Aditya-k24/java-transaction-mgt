package app.model;

public class Person {

    private String personId;
    private String name;
    private int balance;
    private String accountNumber; // Changed from companyId

    public Person() {

    }

    public Person(String personId, String name, int balance, String accountNumber) {
        super();
        this.personId = personId;
        this.name = name;
        this.balance = balance;
        this.accountNumber = accountNumber;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Person [personId=" + personId + ", name=" + name + ", balance=" + balance
                + ", accountNumber=" + accountNumber + "]"; // Changed from companyId
    }
}
