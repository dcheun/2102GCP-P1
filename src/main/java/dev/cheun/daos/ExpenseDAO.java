package dev.cheun.daos;

import dev.cheun.entities.Expense;

import java.util.Set;

public interface ExpenseDAO {
    // Create
    Expense createExpense(Expense expense);

    // Read
    Set<Expense> getAllExpenses();
    Expense getExpenseById(int id);

    // Update
    Expense updateExpense(Expense expense);

    // Delete
    boolean deleteExpenseById(int id);
}
