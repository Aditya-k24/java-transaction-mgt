package app.model;

public class PersonAccount {

    private String accountNumber;
    private int accountBalance;
    private String accountType;
    private String personId;

    public PersonAccount() {

    }

    public PersonAccount(String accountNumber, int accountBalance, String accountType, String personId) {
        this.accountNumber = accountNumber;
        this.accountBalance = accountBalance;
        this.accountType = accountType;
        this.personId = personId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(int accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    @Override
    public String toString() {
        return "PersonAccount [accountNumber=" + accountNumber + ", accountBalance=" + accountBalance +
               ", accountType=" + accountType + ", personId=" + personId + "]";
    }
}
