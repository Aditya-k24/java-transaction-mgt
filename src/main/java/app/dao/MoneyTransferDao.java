package app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import app.model.MoneyTransfer;

public class MoneyTransferDao implements CrudDao<MoneyTransfer> {

	@Override
	public void add(MoneyTransfer moneyTransfer) throws DaoException {
		try (Connection c = Database.getConnection()) {
			String sql = "insert into money_transfer("
					+ "money_transfer_id, from_person_id, to_person_id, amount, purpose, "
					+ "status, status_details, created_time, transfer_time" + ") values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				ps.setString(1, moneyTransfer.getMoneyTransferId());
				ps.setString(2, moneyTransfer.getFromPersonId());
				ps.setString(3, moneyTransfer.getToPersonId());
				ps.setInt(4, moneyTransfer.getAmount());
				ps.setString(5, moneyTransfer.getPurpose());
				ps.setString(6, moneyTransfer.getStatus());
				ps.setString(7, moneyTransfer.getStatusDetails());
				ps.setTimestamp(8, moneyTransfer.getCreatedTime());
				ps.setTimestamp(9, moneyTransfer.getTransferTime());
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			throw new DaoException("cannot add money transfer: " + e.getMessage(), e);
		}
	}

	@Override
	public void update(MoneyTransfer moneyTransfer) throws DaoException {
		try (Connection c = Database.getConnection()) {
			String sql = "update money_transfer set from_person_id = ?, to_person_id = ?, amount = ?, purpose = ? where money_transfer_id = ? ";
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				ps.setString(1, moneyTransfer.getFromPersonId());
				ps.setString(2, moneyTransfer.getToPersonId());
				ps.setInt(3, moneyTransfer.getAmount());
				ps.setString(4, moneyTransfer.getPurpose());
				ps.setString(5, moneyTransfer.getMoneyTransferId());

				if (1 != ps.executeUpdate()) {
					throw new DaoException("cannot update money transfer " + moneyTransfer.getMoneyTransferId()
							+ ": record not found");
				}
			}
		} catch (SQLException e) {
			throw new DaoException(
					"cannot update money transfer " + moneyTransfer.getMoneyTransferId() + ": " + e.getMessage(), e);
		}
	}

	@Override
	public void delete(String moneyTransferId) throws DaoException {
		try (Connection c = Database.getConnection()) {
			String sql = "delete from money_transfer where money_transfer_id = ?";
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				ps.setString(1, moneyTransferId);
				if (1 != ps.executeUpdate()) {
					throw new DaoException("cannot delete money transfer " + moneyTransferId + ": record not found");
				}
			}
		} catch (SQLException e) {
			throw new DaoException("cannot delete money transfer " + moneyTransferId + ": " + e.getMessage(), e);
		}
	}

	@Override
	public List<MoneyTransfer> list() {
		try (Connection c = Database.getConnection()) {
			String sql = "select * from money_transfer";
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				return resultSetToList(ps.executeQuery());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<MoneyTransfer> filter(MoneyTransfer values) {
		try (Connection c = Database.getConnection()) {
			List<Object> params = new ArrayList<>();
			StringBuilder sql = new StringBuilder();
			sql.append("select * from money_transfer");
			List<String> whereClause = new ArrayList<>();
			if (values.getFromPersonId() != null && !values.getFromPersonId().isEmpty()) {
				whereClause.add("from_person_id = ?");
				params.add(values.getFromPersonId());
			}
			if (values.getToPersonId() != null && !values.getToPersonId().isEmpty()) {
				whereClause.add("to_person_id = ?");
				params.add(values.getToPersonId());
			}
			if (params.size() > 0) {
				sql.append(" where ").append(String.join(" and ", whereClause));
			}
			try (PreparedStatement ps = c.prepareStatement(sql.toString())) {
				for (int i = 1; i <= params.size(); i++) {
					ps.setObject(i, params.get(i - 1));
				}
				return resultSetToList(ps.executeQuery());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Optional<MoneyTransfer> findById(String moneyTransferId) {
		try (Connection c = Database.getConnection()) {
			String sql = "select * from money_transfer where money_transfer_id = ?";
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				ps.setString(1, moneyTransferId);
				List<MoneyTransfer> list = resultSetToList(ps.executeQuery());
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
	public List<MoneyTransfer> resultSetToList(ResultSet resultSet) throws SQLException {
		List<MoneyTransfer> list = new ArrayList<>();
		try (ResultSet rs = resultSet) {
			while (rs.next()) {
				MoneyTransfer moneyTransfer = new MoneyTransfer();
				moneyTransfer.setMoneyTransferId(rs.getString("money_transfer_id"));
				moneyTransfer.setFromPersonId(rs.getString("from_person_id"));
				moneyTransfer.setToPersonId(rs.getString("to_person_id"));
				moneyTransfer.setAmount(rs.getInt("amount"));
				moneyTransfer.setPurpose(rs.getString("purpose"));
				moneyTransfer.setStatus(rs.getString("status"));
				moneyTransfer.setStatusDetails(rs.getString("status_details"));
				moneyTransfer.setCreatedTime(rs.getTimestamp("created_time"));
				moneyTransfer.setTransferTime(rs.getTimestamp("transfer_time"));
				list.add(moneyTransfer);
			}
		}
		return list;
	}

	public List<MoneyTransfer> findAllByStatus(String status, int count) {
		String sql = "select * "
				+ "from money_transfer "
				+ "where status = ? "
				+ "order by created_time "
				+ "fetch first 10 rows only";
		try (Connection c = Database.getConnection()) {
			try (PreparedStatement ps = c.prepareStatement(sql)) {
				ps.setString(1, status);
				return resultSetToList(ps.executeQuery());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
