package dev.cheun.daos;

import dev.cheun.entities.ExpenseStatus;

import java.util.Set;

public interface ExpenseStatusDAO {
    // Create
    ExpenseStatus createExpenseStatus(ExpenseStatus expenseStatus);

    // Read
    Set<ExpenseStatus> getAllExpenseStatuses();
    ExpenseStatus getExpenseStatusById(int id);

    // Update
    ExpenseStatus updateExpenseStatus(ExpenseStatus expenseStatus);

    // Delete
    boolean deleteExpenseStatusById(int id);
}
