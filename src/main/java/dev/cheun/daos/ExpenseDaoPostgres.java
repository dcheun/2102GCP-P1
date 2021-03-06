package dev.cheun.daos;

import dev.cheun.entities.Expense;
import dev.cheun.exceptions.NotFoundException;
import dev.cheun.utils.ConnectionUtil;
import dev.cheun.utils.DateTimeUtil;
import org.apache.log4j.Logger;

import java.sql.*;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

public class ExpenseDaoPostgres implements ExpenseDAO {

    private static final Logger logger = Logger.getLogger(ExpenseDaoPostgres.class.getName());

    @Override
    public Expense createExpense(Expense expense) {
        try(Connection conn = ConnectionUtil.createConnection()) {
            String sql = "INSERT INTO expense " +
                    "(amount_in_cents, employee_id, submitted_at) " +
                    "VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);
            // Set the field params.
            ps.setInt(1, expense.getAmountInCents());
            ps.setInt(2, expense.getEmployeeId());
            try {
                ps.setTimestamp(3,
                        DateTimeUtil.offsetDateTimeToTimestamp(expense.getSubmittedAt()));
            } catch (NullPointerException e) {
                ps.setTimestamp(3, DateTimeUtil.getTimestampUtcNow());
            }
            ps.execute();
            // Return the val of the generated keys.
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int key = rs.getInt("id");
            expense.setId(key);
            logger.info("createExpense: " + expense);
            return expense;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("createExpense: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Set<Expense> getAllExpenses() {
        try(Connection conn = ConnectionUtil.createConnection()) {
            String sql = "SELECT * FROM expense";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            Set<Expense> expenses = new HashSet<>();
            // While there exist records...
            while(rs.next()) {
                Expense expense = new Expense();
                expense.setId(rs.getInt("id"));
                expense.setAmountInCents(rs.getInt("amount_in_cents"));
                expense.setReason(rs.getString("reason"));
                expense.setSubmittedAt(DateTimeUtil.timestampToOffsetDateTime(
                        rs.getTimestamp("submitted_at")
                ));
                try {
                    expense.setMgrReviewedAt(DateTimeUtil.timestampToOffsetDateTime(
                            rs.getTimestamp("submitted_at")
                    ));
                } catch (NullPointerException e) {
                    expense.setMgrReviewedAt(null);
                }
                expense.setStatusId(rs.getInt("status_id"));
                expense.setEmployeeId(rs.getInt("employee_id"));
                expense.setManagerId(rs.getInt("manager_id"));
                expenses.add(expense);
            }
            logger.info("getAllExpenses: size=" + expenses.size());
            return expenses;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("getAllExpense: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Expense getExpenseById(int id) {
        try(Connection conn = ConnectionUtil.createConnection()) {
            String sql = "SELECT * FROM expense WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(!rs.next()) {
                throw new NotFoundException("No such expense exists");
            }
            Expense expense = new Expense();
            expense.setId(rs.getInt("id"));
            expense.setAmountInCents(rs.getInt("amount_in_cents"));
            expense.setReason(rs.getString("reason"));
            expense.setSubmittedAt(DateTimeUtil.timestampToOffsetDateTime(
                    rs.getTimestamp("submitted_at")
            ));
            try {
                expense.setMgrReviewedAt(DateTimeUtil.timestampToOffsetDateTime(
                        rs.getTimestamp("submitted_at")
                ));
            } catch (NullPointerException e) {
                expense.setMgrReviewedAt(null);
            }
            expense.setStatusId(rs.getInt("status_id"));
            expense.setEmployeeId(rs.getInt("employee_id"));
            expense.setManagerId(rs.getInt("manager_id"));
            logger.info("getExpenseById: id=" + id);
            return expense;
        } catch(SQLException e) {
            e.printStackTrace();
            logger.error("getExpenseById: Unable to get record with id=" + id);
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Expense updateExpense(Expense expense) {
        try(Connection conn = ConnectionUtil.createConnection()) {
            String sql = "UPDATE expense " +
                    "SET amount_in_cents = ?, " +
                    "reason = ?, " +
                    "submitted_at = ?, " +
                    "mgr_reviewed_at = ?, " +
                    "status_id = ?, " +
                    "employee_id = ?, " +
                    "manager_id = ? " +
                    "WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, expense.getAmountInCents());
            ps.setString(2, expense.getReason());
            try {
                ps.setTimestamp(3,
                        DateTimeUtil.offsetDateTimeToTimestamp(expense.getSubmittedAt()));
            } catch (NullPointerException e) {
                ps.setTimestamp(3, DateTimeUtil.getTimestampUtcNow());
            }
            try {
                ps.setTimestamp(4, Timestamp.valueOf(
                        expense.getMgrReviewedAt().atZoneSameInstant(ZoneOffset.UTC)
                                .toLocalDateTime()));
            } catch (NullPointerException e) {
                ps.setTimestamp(4, null);
            }
            try {
                ps.setTimestamp(4,
                        DateTimeUtil.offsetDateTimeToTimestamp(expense.getMgrReviewedAt()));
            } catch (NullPointerException e) {
                ps.setTimestamp(4, null);
            }
            ps.setInt(5, expense.getStatusId());
            ps.setInt(6, expense.getEmployeeId());
            if (expense.getManagerId() > 0) {
                ps.setInt(7, expense.getManagerId());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            ps.setInt(8, expense.getId());
            int rowCount = ps.executeUpdate();
            if(rowCount == 0) {
                throw new NotFoundException("No such expense exists");
            }
            logger.info("updateExpense: " + expense);
            return expense;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("updateExpense: Unable to update: " + expense);
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean deleteExpenseById(int id) {
        try(Connection conn = ConnectionUtil.createConnection()) {
            String sql = "DELETE FROM expense WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int rowCount = ps.executeUpdate();
            if(rowCount == 0) {
                throw new NotFoundException("Unable to delete: No such expense exists");
            }
            logger.info("deleteExpenseById: id=" +id);
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
            logger.error("deleteExpenseById: Unable to delete record with id=" + id);
            logger.error(e.getMessage());
            return false;
        }
    }
}
