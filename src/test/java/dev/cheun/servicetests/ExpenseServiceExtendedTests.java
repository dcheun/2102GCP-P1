package dev.cheun.servicetests;

import dev.cheun.daos.ExpenseDAO;
import dev.cheun.entities.Expense;
import dev.cheun.services.ExpenseService;
import dev.cheun.services.ExpenseServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceExtendedTests {
    @Mock
    private ExpenseDAO expenseDAO = null;
    private ExpenseService expenseService = null;

    @BeforeEach
    void setUp() {
        Expense e1 = new Expense(
                1,
                10_000,
                1);
        Expense e2 = new Expense(
                2,
                20_000,
                2);
        Expense e3 = new Expense(
                3,
                30_000,
                1);
        Expense e4 = new Expense(
                4,
                40_000,
                1);
        Set<Expense> expenseSet = new HashSet<>(Arrays.asList(e1, e2, e3, e4));
        Mockito.when(expenseDAO.getAllExpenses()).thenReturn(expenseSet);
        this.expenseService = new ExpenseServiceImpl(this.expenseDAO);
    }

    @Test
    void get_expenses_by_emp_id() {
        Set<Expense> expenseSet = this.expenseService.getExpensesByEmployeeId(1);
        Assertions.assertEquals(3, expenseSet.size());
    }
}
