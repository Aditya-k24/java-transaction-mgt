package app.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import app.dao.DaoException;
import app.dao.Database;
import app.dao.MoneyTransferDao;
import app.model.MoneyTransfer;
import app.model.Person;

public class MoneyTransferService implements CrudService<MoneyTransfer> {

    private PersonService personService = new PersonService();
    private MoneyTransferDao dao = new MoneyTransferDao();

    public void transfer(String moneyTransferId, Timestamp createdTime, String fromPersonId, String toPersonId,
                        String fromAccountId, String toAccountId, int amount, String purpose)
            throws ServiceException {
        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setMoneyTransferId(moneyTransferId);
        moneyTransfer.setCreatedTime(createdTime);
        moneyTransfer.setFromPersonId(fromPersonId);
        moneyTransfer.setToPersonId(toPersonId);
        moneyTransfer.setFromAccountId(fromAccountId); // Set fromAccountId
        moneyTransfer.setToAccountId(toAccountId); // Set toAccountId
        moneyTransfer.setAmount(amount);
        moneyTransfer.setPurpose(purpose);
        moneyTransfer.setStatus("SUBMITTED");
        moneyTransfer.setStatusDetails("");
        moneyTransfer.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        add(moneyTransfer);
    }

    public void execute(MoneyTransfer moneyTransfer) throws ServiceException {
        Person from, to;
        try {
            from = validateAccountId(moneyTransfer.getFromAccountId(), "From Account ID");
            to = validateAccountId(moneyTransfer.getToAccountId(), "To Account ID");
            validateBalance(moneyTransfer, from);
        } catch (ValidationException e) {
            moneyTransfer.setStatus("ERROR");
            moneyTransfer.setStatusDetails(e.getMessage());
            update(moneyTransfer);
            return;
        }
        try {
            Database.startTransaction();
            from.setBalance(from.getBalance() - moneyTransfer.getAmount());
            personService.update(from);
            to.setBalance(to.getBalance() + moneyTransfer.getAmount());
            personService.update(to);
            moneyTransfer.setTransferTime(new Timestamp(System.currentTimeMillis()));
            moneyTransfer.setStatus("COMPLETED");
            update(moneyTransfer);
            Database.commit();
        } catch (SQLException | ServiceException e) {
            moneyTransfer.setStatus("ERROR");
            moneyTransfer.setStatusDetails(e.getMessage());
            try {
                Database.rollback();
            } catch (SQLException e2) {
                moneyTransfer.setStatusDetails(e.getMessage() + "\n" + e2.getMessage());
            }
            update(moneyTransfer);
        } finally {
            Database.closeTransaction();
        }
    }

    public void validate(MoneyTransfer moneyTransfer) throws ValidationException {
        Person from = validatePersonId(moneyTransfer.getFromPersonId(), "From Person ID");
        validatePersonId(moneyTransfer.getToPersonId(), "To Person ID");
        validateBalance(moneyTransfer, from);
    }

    private Person validatePersonId(String personId, String fieldName) throws ValidationException {
        Optional<Person> personOptional = personService.findById(personId);
        if (personOptional.isEmpty()) {
            throw new ValidationException("Invalid " + fieldName + " " + personId);
        }
        return personOptional.get();
    }

    private Person validateAccountId(String accountId, String fieldName) throws ValidationException {
        Optional<Person> personOptional = personService.findByAccountId(accountId);
        if (personOptional.isEmpty()) {
            throw new ValidationException("Invalid " + fieldName + " " + accountId);
        }
        return personOptional.get();
    }

    private void validateBalance(MoneyTransfer moneyTransfer, Person from) throws ValidationException {
        if (from.getBalance() < moneyTransfer.getAmount()) {
            throw new ValidationException("Insufficient balance");
        }
    }

    @Override
    public void add(MoneyTransfer moneyTransfer) throws ServiceException {
        validate(moneyTransfer);
        try {
            dao.add(moneyTransfer);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void update(MoneyTransfer moneyTransfer) throws ServiceException {
        validate(moneyTransfer);
        try {
            dao.update(moneyTransfer);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(String moneyTransferId) throws ServiceException {
        try {
            dao.delete(moneyTransferId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<MoneyTransfer> list() {
        return dao.list();
    }

    @Override
    public List<MoneyTransfer> filter(MoneyTransfer values) {
        return dao.filter(values);
    }

    @Override
    public Optional<MoneyTransfer> findById(String moneyTransferId) {
        return dao.findById(moneyTransferId);
    }

    public List<MoneyTransfer> findAllByStatus(String status, int count) {
        return dao.findAllByStatus(status, count);
    }
}
