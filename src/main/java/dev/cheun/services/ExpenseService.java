package dev.cheun.services;

import dev.cheun.entities.Expense;

import java.util.Set;

// Business Logic
public interface ExpenseService {
    // CREATE
    Expense createExpense(Expense expense);

    // READ
    Set<Expense> getAllExpenses();
    Set<Expense> getExpensesByEmployeeId(int employeeId);
    Expense getExpenseById(int id);

    // UPDATE
    Expense updateExpense(Expense expense);

    // DELETE
    boolean deleteExpenseById(int id);
}
