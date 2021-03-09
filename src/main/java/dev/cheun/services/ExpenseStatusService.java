package dev.cheun.services;

import dev.cheun.entities.ExpenseStatus;

import java.util.Set;

public interface ExpenseStatusService {
    // Create
    ExpenseStatus createStatus(ExpenseStatus status);

    // Read
    Set<ExpenseStatus> getAllStatuses();

    ExpenseStatus getStatusById(int id);

    // Update
    ExpenseStatus updateStatus(ExpenseStatus status);

    // Delete
    boolean deleteStatus(int id);
}
