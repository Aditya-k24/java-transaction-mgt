package app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import app.dao.Database;

public class DbSchema {

	public static void main(String[] args) {
		List<String> sqls = readSqlsFromSchemaFile();
		Database.startDerbyDatabase();
		try (Connection c = Database.getConnection()) {
			for (String sql : sqls) {
				executeSql(sql, c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Database.stopDerbyDatabase();
		}
	}

	private static List<String> readSqlsFromSchemaFile() {
		Path path = Path.of("./src/main/resources/schema.sql");
		try {
			String contents = Files.readString(path);
			List<String> sqls = new ArrayList<>();
			for (String part : contents.split("\\;")) {
				StringBuilder sql = new StringBuilder();
				for (String line : part.split("[\\r\\n]")) {
					if (line.isBlank() || line.trim().startsWith("--")) {
						continue;
					}
					sql.append(line.trim()).append(" ");
				}
				if (sql.length() > 0) {
					sqls.add(sql.toString());
				}
			}
			return sqls;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void executeSql(String sql, Connection c) {
		try (Statement s = c.createStatement()) {
			System.out.println(sql);
			s.executeUpdate(sql);
			System.out.println(" SUCCESS");
		} catch (SQLException e) {
			System.out.println("  ERROR: " + e.getErrorCode() + " " + e.getSQLState() + ": " + e.getMessage());
		}
	}

	public static void executeSql_old(String sql, Connection c) {
		Statement s = null;
		try {
			s = c.createStatement();
			System.out.println(sql);
			s.executeUpdate(sql);
			System.out.println(" SUCCESS");
			s.close();
		} catch (SQLException e) {
			System.out.println("  ERROR: " + e.getErrorCode() + " " + e.getSQLState() + ": " + e.getMessage());
		} finally {
			if (s != null) {
				try {
					s.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
