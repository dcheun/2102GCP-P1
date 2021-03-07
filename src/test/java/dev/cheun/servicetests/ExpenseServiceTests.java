package dev.cheun.servicetests;

import dev.cheun.daos.AppUserDaoPostgres;
import dev.cheun.daos.ExpenseDaoPostgres;
import dev.cheun.entities.AppUser;
import dev.cheun.entities.Expense;
import dev.cheun.exceptions.NotAuthorizedException;
import dev.cheun.services.AppUserService;
import dev.cheun.services.AppUserServiceImpl;
import dev.cheun.services.ExpenseService;
import dev.cheun.services.ExpenseServiceImpl;
import dev.cheun.utils.DateTimeUtil;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExpenseServiceTests {
    private static final ExpenseService serv = new ExpenseServiceImpl(new ExpenseDaoPostgres());
    private static final AppUserService aServ = new AppUserServiceImpl(new AppUserDaoPostgres());
    private static AppUser testEmp = null;
    private static AppUser testMgr = null;
    private static Expense testE1 = null;

    @BeforeAll
    public static void setUpOnce() {
        // Set up test resources.
        AppUser ron = new AppUser(0, "Ron", "Weasley", "rweasley@hogwarts.edu", 1, "weasley123");
        AppUser hagrid = new AppUser(0, "Rubeus", "Hagrid", "rhagrid@hogwarts.edu", 2, "hagrid123");
        testEmp = aServ.registerAppUser(ron);
        testMgr = aServ.registerAppUser(hagrid);
    }

    @AfterAll
    public static void tearDownOnce() {
        serv.deleteExpenseById(testE1.getId());
        aServ.deleteAppUserById(testEmp.getId());
        aServ.deleteAppUserById(testMgr.getId());
    }

    @Test
    @Order(1)
    void create_expense() {
        Expense expense = new Expense(0, 10_000, testEmp.getId());
        serv.createExpense(expense);
        Assertions.assertEquals(10_000, expense.getAmountInCents());
        // Should default to status of 1 (PENDING)
        Assertions.assertEquals(1, expense.getStatusId());
        testE1 = expense;
    }

    @Test
    @Order(2)
    void update_expense_by_emp_success() {
        Expense expense = new Expense(testE1.getId(), 20_000, testE1.getEmployeeId());
        serv.updateExpense(expense);
        Assertions.assertEquals(20_000, expense.getAmountInCents());
    }

    // Employee tries to update fields that they don't have access to.
    @Test
    @Order(3)
    void update_expense_by_emp_unauth() {
        Expense expense = new Expense(
                testE1.getId(),
                10_000,
                "I need money",
                DateTimeUtil.getOffsetDateTimeUtcNow(),
                DateTimeUtil.getOffsetDateTimeUtcNow(),
                2,
                testEmp.getId(),
                0);
        Exception e = Assertions.assertThrows(NotAuthorizedException.class, () -> {
            serv.updateExpense(expense);
        });
        String expectedMessage = "Manager required to update";
        Assertions.assertTrue(e.getMessage().contains(expectedMessage));
    }

    // Test if manager_id set is actually a manager.
    // Should throw NotAuthorizedException
    @Test
    @Order(4)
    void update_expense_by_fake_mgr() {
        Expense expense = new Expense(
                testE1.getId(),
                testE1.getAmountInCents(),
                "Out of money",
                DateTimeUtil.getOffsetDateTimeUtcNow(),
                DateTimeUtil.getOffsetDateTimeUtcNow(),
                3,
                testEmp.getId(),
                testEmp.getId()
        );
        Exception e = Assertions.assertThrows(NotAuthorizedException.class, () -> {
            serv.updateExpense(expense);
        });
        String expectedMessage = "Not a manager";
        Assertions.assertTrue(e.getMessage().contains(expectedMessage));
    }

    @Test
    @Order(5)
    void update_expense_by_mgr() {
        Expense expense = new Expense(
                testE1.getId(),
                testE1.getAmountInCents(),
                "Out of money",
                DateTimeUtil.getOffsetDateTimeUtcNow(),
                DateTimeUtil.getOffsetDateTimeUtcNow(),
                3,
                testEmp.getId(),
                testMgr.getId()
        );
        serv.updateExpense(expense);
        Assertions.assertTrue(expense.getReason().equals("Out of money"));
        Assertions.assertNotNull(expense.getMgrReviewedAt());
        Assertions.assertEquals(3, expense.getStatusId());
    }
}
