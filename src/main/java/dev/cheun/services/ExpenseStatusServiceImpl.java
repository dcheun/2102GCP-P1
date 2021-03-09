package dev.cheun.services;

import dev.cheun.daos.ExpenseStatusDAO;
import dev.cheun.entities.ExpenseStatus;

import java.util.Set;

public class ExpenseStatusServiceImpl implements ExpenseStatusService {

    private final ExpenseStatusDAO dao;

    public ExpenseStatusServiceImpl(ExpenseStatusDAO dao) {
        this.dao = dao;
    }

    @Override
    public ExpenseStatus createStatus(ExpenseStatus status) {
        return this.dao.createExpenseStatus(status);
    }

    @Override
    public Set<ExpenseStatus> getAllStatuses() {
        return this.dao.getAllExpenseStatuses();
    }

    @Override
    public ExpenseStatus getStatusById(int id) {
        return this.dao.getExpenseStatusById(id);
    }

    @Override
    public ExpenseStatus updateStatus(ExpenseStatus status) {
        return this.dao.updateExpenseStatus(status);
    }

    @Override
    public boolean deleteStatus(int id) {
        return this.dao.deleteExpenseStatusById(id);
    }
}
