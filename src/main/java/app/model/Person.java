package app.model;

public class Person {

    private String personId;
    private String name;
    private int balance;
    private String accountId; // Changed from companyId

    public Person() {

    }

    public Person(String personId, String name, int balance, String accountId) {
        super();
        this.personId = personId;
        this.name = name;
        this.balance = balance;
        this.accountId = accountId;
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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
                + ", accountId=" + accountId + "]"; // Changed from companyId
    }
}
