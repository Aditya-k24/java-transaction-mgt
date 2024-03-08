package app.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<T> {

	void add(T item) throws ServiceException;

	void update(T item) throws ServiceException;

	void delete(String id) throws ServiceException;

	List<T> list();

	List<T> filter(T values);

	Optional<T> findById(String id);

	void validate(T item) throws ValidationException;

}
