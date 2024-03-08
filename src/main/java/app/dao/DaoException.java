package app.dao;

import java.sql.SQLException;

public class DaoException extends Exception {

	private static final long serialVersionUID = 1L;

	private final SQLException sqlException;

	public DaoException(String message, SQLException sqlException) {
		super(message, sqlException);
		this.sqlException = sqlException;
	}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
		sqlException = null;
	}

	public DaoException(String message) {
		super(message);
		sqlException = null;
	}

	public SQLException getSqlException() {
		return sqlException;
	}

}
