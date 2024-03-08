package app.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

//import app.model.Person;

public interface CrudDao<T> {

	void add(T item) throws DaoException;

	void update(T item) throws DaoException;

	void delete(String id) throws DaoException;

	List<T> list();

	List<T> filter(T values);

	Optional<T> findById(String id);

	List<T> resultSetToList(ResultSet rs) throws SQLException;

}
