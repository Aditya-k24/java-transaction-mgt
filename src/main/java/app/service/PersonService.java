package app.service;

import java.util.List;
import java.util.Optional;

import app.dao.DaoException;
import app.dao.PersonDao;
import app.model.Person;

public class PersonService implements CrudService<Person> {

    private PersonDao personDao = new PersonDao();

    @Override
    public void add(Person person) throws ServiceException {
        validate(person);
        try {
            personDao.add(person);

        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void update(Person person) throws ServiceException {
        validate(person);
        try {
            personDao.update(person);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(String personId) throws ServiceException {
        try {
            personDao.delete(personId);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Person> findById(String personID) {
        return personDao.findById(personID);
    }

    @Override
    public List<Person> filter(Person values) {
        return personDao.filter(values);
    }

    @Override
    public List<Person> list() {
        return personDao.list();
    }

    @Override
    public void validate(Person person) throws ValidationException {
        if (!person.getName().matches("^[A-Za-z0-9-]*$")) {
            throw new ValidationException(
                    "Invalid account code: it can only contain alphanumeric characters and hyphen.");
        }
    }

    public Optional<Person> findByAccountId(String accountId) {
        return personDao.findByAccountId(accountId);
    }
}
