package app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import app.model.Person;

public class PersonDao implements CrudDao<Person> {

    @Override
    public void add(Person person) throws DaoException {
        try (Connection c = Database.getConnection()) {
            String sql = "insert into person(person_id, name, net_worth, balance, account_id) values (?,?,?,?,?)";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, person.getPersonId());
                ps.setString(2, person.getName());
                ps.setInt(4, person.getBalance());
                ps.setString(5, person.getAccountId());
                ps.execute();
            }
        } catch (SQLException e) {
            throw new DaoException("cannot add person: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String personID) throws DaoException {
        try (Connection c = Database.getConnection()) {
            String sql = "delete from person where person_id = ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, personID);
                if (1 != ps.executeUpdate()) {
                    throw new DaoException("cannot delete person " + personID + ": record not found");
                }
            }
        } catch (SQLException e) {
            throw new DaoException("cannot delete person " + personID + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Person person) throws DaoException {
        try (Connection c = Database.getConnection()) {
            String sql = "update person set name = ?, net_worth = ? , balance = ?, account_id = ? where person_id = ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, person.getName());
                ps.setInt(3, person.getBalance());
                ps.setString(4, person.getAccountId());
                ps.setString(5, person.getPersonId());
                if (1 != ps.executeUpdate()) {
                    throw new DaoException(("cannot update person" + person.getPersonId() + "; record not found"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("cannot update person" + person.getPersonId() + ":" + e.getMessage(), e);
        }
    }

    private List<Person> filterByName(String toBeFiltered) {
        try (Connection c = Database.getConnection()) {
            String sql = "select * from person where name like ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, toBeFiltered + "%");
                return resultSetToList(ps.executeQuery());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Person> filter(Person values) {
        return filterByName(values.getName());
    }

    @Override
    public Optional<Person> findById(String personID) {
        try (Connection c = Database.getConnection()) {
            String sql = "select * from person where person_id = ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, personID);
                List<Person> list = resultSetToList(ps.executeQuery());
                if (list.size() == 1) {
                    return Optional.of(list.get(0));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Person> list() {
        try (Connection c = Database.getConnection()) {
            String sql = "select * from person";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                return resultSetToList(ps.executeQuery());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Person> resultSetToList(ResultSet resultSet) throws SQLException {
        List<Person> list = new ArrayList<>();
        try (ResultSet rs = resultSet) {
            while (rs.next()) {
                Person person = new Person();
                person.setPersonId(rs.getString("person_id"));
                person.setName(rs.getString("name"));
                person.setBalance(rs.getInt("balance"));
                list.add(person);
            }
        }
        return list;
    }

	public Optional<Person> findByAccountId(String accountId) {
        try (Connection c = Database.getConnection()) {
            String sql = "select * from person where account_id = ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, accountId);
                List<Person> list = resultSetToList(ps.executeQuery());
                if (list.size() == 1) {
                    return Optional.of(list.get(0));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
