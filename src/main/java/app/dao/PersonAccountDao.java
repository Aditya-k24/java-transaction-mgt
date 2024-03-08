package app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import app.model.PersonAccount;

public class PersonAccountDao implements CrudDao<PersonAccount> {

    @Override
    public void add(PersonAccount personAccount) throws DaoException {
        try (Connection c = Database.getConnection()) {
            String sql = "INSERT INTO person_account(account_number, account_balance, account_type, person_id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, personAccount.getAccountNumber());
                ps.setInt(2, personAccount.getAccountBalance());
                ps.setString(3, personAccount.getAccountType());
                ps.setString(4, personAccount.getPersonId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DaoException("Cannot add person account: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) throws DaoException {
        try (Connection c = Database.getConnection()) {
            String sql = "DELETE FROM person_account WHERE account_number = ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, id);
                if (ps.executeUpdate() != 1) {
                    throw new DaoException("Failed to delete person account with ID: " + id);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error deleting person account with ID: " + id, e);
        }
    }

    @Override
    public void update(PersonAccount personAccount) throws DaoException {
        try (Connection c = Database.getConnection()) {
            String sql = "UPDATE person_account SET account_balance = ?, account_type = ?, person_id = ? WHERE account_number = ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, personAccount.getAccountBalance());
                ps.setString(2, personAccount.getAccountType());
                ps.setString(3, personAccount.getPersonId());
                ps.setString(4, personAccount.getAccountNumber());
                if (ps.executeUpdate() != 1) {
                    throw new DaoException("Failed to update person account with ID: " + personAccount.getAccountNumber());
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error updating person account with ID: " + personAccount.getAccountNumber(), e);
        }
    }

    @Override
    public List<PersonAccount> list() {
        List<PersonAccount> personAccounts = new ArrayList<>();
        try (Connection c = Database.getConnection()) {
            String sql = "SELECT * FROM person_account";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    PersonAccount personAccount = new PersonAccount();
                    personAccount.setAccountNumber(rs.getString("account_number"));
                    personAccount.setAccountBalance(rs.getInt("account_balance"));
                    personAccount.setAccountType(rs.getString("account_type"));
                    personAccount.setPersonId(rs.getString("person_id"));
                    personAccounts.add(personAccount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return personAccounts;
    }

    @Override
    public List<PersonAccount> filter(PersonAccount values) {
        List<PersonAccount> filteredList = new ArrayList<>();
        try (Connection c = Database.getConnection()) {
            String sql = "SELECT * FROM person_account WHERE account_number = ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, values.getAccountNumber());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    PersonAccount personAccount = new PersonAccount();
                    personAccount.setAccountNumber(rs.getString("account_number"));
                    personAccount.setAccountBalance(rs.getInt("account_balance"));
                    personAccount.setAccountType(rs.getString("account_type"));
                    personAccount.setPersonId(rs.getString("person_id"));
                    filteredList.add(personAccount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
        return filteredList;
    }


    @Override
    public Optional<PersonAccount> findById(String id) {
        try (Connection c = Database.getConnection()) {
            String sql = "SELECT * FROM person_account WHERE account_number = ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    PersonAccount personAccount = new PersonAccount();
                    personAccount.setAccountNumber(rs.getString("account_number"));
                    personAccount.setAccountBalance(rs.getInt("account_balance"));
                    personAccount.setAccountType(rs.getString("account_type"));
                    personAccount.setPersonId(rs.getString("person_id"));
                    return Optional.of(personAccount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return Optional.empty();
    }

    @Override
    public List<PersonAccount> resultSetToList(ResultSet rs) throws SQLException {
        List<PersonAccount> personAccounts = new ArrayList<>();
        while (rs.next()) {
            PersonAccount personAccount = new PersonAccount();
            personAccount.setAccountNumber(rs.getString("account_number"));
            personAccount.setAccountBalance(rs.getInt("account_balance"));
            personAccount.setAccountType(rs.getString("account_type"));
            personAccount.setPersonId(rs.getString("person_id"));
            personAccounts.add(personAccount);
        }
        return personAccounts;
    }
}
