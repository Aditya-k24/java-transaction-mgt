package app.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

	private static String url = "jdbc:derby:" + System.getProperty("user.home") + "/app";

	public static void startDerbyDatabase() {
		try (Connection conn = DriverManager.getConnection(url + ";create=true")) {
			System.out.println("Startup Completed..");
		} catch (SQLException e) {
			throw new RuntimeException("Unable to start database", e);
		}
	}

	public static void stopDerbyDatabase() {
		try (Connection conn = DriverManager.getConnection(url + ";shutdown=true")) {
			System.out.println("// will not reach here...");
		} catch (SQLException e) {
			System.out.println("Shutdown completed... " + e.getSQLState() + " " + e.getErrorCode());
		}
	}

	private static ThreadLocal<Connection> threadConnection = new ThreadLocal<>();

	public static Connection getConnection() throws SQLException {
		if (threadConnection.get() != null) {
			return makeProxy(threadConnection.get());
		}
		return DriverManager.getConnection(url);
	}

	private static Connection makeProxy(Connection connection) {
		return (Connection) Proxy.newProxyInstance(Database.class.getClassLoader(), new Class<?>[] { Connection.class },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if (method.getName().equals("close")) {
							return null;
						}
						return method.invoke(connection, args);
					}
				});
	}

	public static void startTransaction() throws SQLException {
		if (threadConnection.get() != null) {
			throw new SQLException("Transaction already exists");
		}
		Connection connection = DriverManager.getConnection(url);
		connection.setAutoCommit(false);
		threadConnection.set(connection);
	}

	public static void commit() throws SQLException {
		if (threadConnection.get() == null) {
			throw new SQLException("Transaction does not exist");
		}
		try (Connection connection = threadConnection.get()) {
			connection.commit();
		} finally {
			threadConnection.remove();
		}
	}

	public static void rollback() throws SQLException {
		if (threadConnection.get() == null) {
			throw new SQLException("Transaction does not exist");
		}
		try (Connection connection = threadConnection.get()) {
			connection.commit();
		} finally {
			threadConnection.remove();
		}
	}

	public static void closeTransaction() {
		if (threadConnection.get() == null) {
			return;
		}
		try {
			rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
