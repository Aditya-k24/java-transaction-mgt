package app.model;

import java.sql.Timestamp;

public class MoneyTransfer {

    private String moneyTransferId;
    private Timestamp createdTime;
    private String fromPersonId;
    private String toPersonId;
    private String fromAccountId; // New attribute
    private String toAccountId;   // New attribute
    private int amount;
    private String purpose;
    private Timestamp transferTime;
    private String status;
    private String statusDetails;

    public MoneyTransfer(String moneyTransferId, String fromPersonId, String toPersonId, String fromAccountId, String toAccountId, int amount,
            String purpose) {
        this.moneyTransferId = moneyTransferId;
        this.fromPersonId = fromPersonId;
        this.toPersonId = toPersonId;
        this.fromAccountId = fromAccountId; // Initialize fromAccountId
        this.toAccountId = toAccountId;     // Initialize toAccountId
        this.amount = amount;
        this.purpose = purpose;
    }

    public MoneyTransfer() {

    }

    public String getMoneyTransferId() {
        return moneyTransferId;
    }

    public void setMoneyTransferId(String moneyTransferId) {
        this.moneyTransferId = moneyTransferId;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Timestamp getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(Timestamp transferTime) {
        this.transferTime = transferTime;
    }

    public String getFromPersonId() {
        return fromPersonId;
    }

    public void setFromPersonId(String fromPersonId) {
        this.fromPersonId = fromPersonId;
    }

    public String getToPersonId() {
        return toPersonId;
    }

    public void setToPersonId(String toPersonId) {
        this.toPersonId = toPersonId;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDetails() {
        return statusDetails;
    }

    public void setStatusDetails(String statusDetails) {
        this.statusDetails = statusDetails;
    }
}
