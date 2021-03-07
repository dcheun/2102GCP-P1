package dev.cheun.services;

import dev.cheun.daos.AppUserDaoPostgres;
import dev.cheun.daos.ExpenseDAO;
import dev.cheun.entities.Expense;
import dev.cheun.exceptions.NotAuthorizedException;
import dev.cheun.exceptions.NotFoundException;

import java.util.HashSet;
import java.util.Set;

public class ExpenseServiceImpl implements ExpenseService {

    private static ExpenseDAO dao;
    private static AppUserServiceImpl aServ;

    // Dependency injection.
    // A service is created by passing in the dependencies it needs.
    public ExpenseServiceImpl(ExpenseDAO dao) {
        ExpenseServiceImpl.dao = dao;
        aServ = new AppUserServiceImpl(new AppUserDaoPostgres());
    }

    @Override
    public Expense createExpense(Expense expense) {
        // Check if employee exists (throws NotFoundException if not found)
        aServ.getEmployeeById(expense.getEmployeeId());
        return dao.createExpense(expense);
    }

    @Override
    public Set<Expense> getAllExpenses() {
        return dao.getAllExpenses();
    }

    @Override
    public Set<Expense> getExpensesByEmployeeId(int employeeId) {
        Set<Expense> allExpenses = this.getAllExpenses();
        Set<Expense> selectedExpenses = new HashSet<>();
        for (Expense e : allExpenses) {
            if (e.getEmployeeId() == employeeId) {
                selectedExpenses.add(e);
            }
        }
        return selectedExpenses;
    }

    @Override
    public Expense getExpenseById(int id) {
        return dao.getExpenseById(id);
    }

    @Override
    public Expense updateExpense(Expense expense) {
        // If this is a manager updating, then check if the manager_id
        // set is a manager.
        if (expense.getManagerId() != 0) {
            try {
                aServ.getManagerById(expense.getManagerId());
            } catch (NotFoundException e) {
                throw new NotAuthorizedException("Not a manager");
            }
        }
        // Check if fields are modified by employee that they don't have access to.
        Expense currExpense = dao.getExpenseById(expense.getId());
        if (expense.getManagerId() == 0 &&
                expense.getStatusId() != currExpense.getStatusId() &&
                expense.getMgrReviewedAt() != currExpense.getMgrReviewedAt() &&
                expense.getReason() != null &&
                !expense.getReason().equals(currExpense.getReason())
        ) {
            throw new NotAuthorizedException("Manager required to update these fields");
        }
        return dao.updateExpense(expense);
    }

    @Override
    public boolean deleteExpenseById(int id) {
        return dao.deleteExpenseById(id);
    }
}
