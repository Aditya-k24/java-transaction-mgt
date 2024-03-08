package app.service;

// import java.sql.Connection;
// import java.sql.SQLException;
// import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import app.dao.PersonAccountDao;
import app.dao.DaoException;
import app.model.PersonAccount;

public class PersonAccountService implements CrudService<PersonAccount> {

    private PersonAccountDao personAccountDao = new PersonAccountDao();

    @Override
    public void add(PersonAccount personAccount) throws ServiceException {
        validate(personAccount);
        try {
            personAccountDao.add(personAccount);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void update(PersonAccount personAccount) throws ServiceException {
        try {
            personAccountDao.update(personAccount);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) throws ServiceException {
        try {
            personAccountDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<PersonAccount> list() {
        return personAccountDao.list();
    }
    
    
    @Override
    public List<PersonAccount> filter(PersonAccount values) {
        return personAccountDao.filter(values);
    }

    @Override
    public Optional<PersonAccount> findById(String id) {
        return personAccountDao.findById(id);
    }

    @Override
    public void validate(PersonAccount personAccount) throws ValidationException {
        // You can implement validation logic specific to PersonAccount here
    }
}
