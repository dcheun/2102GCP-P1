package dev.cheun.daotests;

import dev.cheun.daos.AppUserDAO;
import dev.cheun.daos.AppUserDaoPostgres;
import dev.cheun.daos.ExpenseDAO;
import dev.cheun.daos.ExpenseDaoPostgres;
import dev.cheun.entities.AppUser;
import dev.cheun.entities.Expense;
import org.junit.jupiter.api.*;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Set;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExpenseDaoTests {
    private static final ExpenseDAO dao = new ExpenseDaoPostgres();
    private static final AppUserDAO aDao = new AppUserDaoPostgres();
    private static AppUser testEmp = null;
    private static AppUser testMgr = null;
    private static Expense testE1 = null;
    private static Expense testE2 = null;
    private static Expense testE3 = null;

    @BeforeAll
    public static void setUpOnce() {
        // Set up test resources.
        AppUser ron = new AppUser(0,"Ron","Weasley","rweasley@hogwarts.edu",1);
        AppUser hagrid = new AppUser(0, "Rubeus", "Hagrid", "rhagrid@hogwarts.edu", 2);
        testEmp = aDao.createAppUser(ron, "weasley123");
        testMgr = aDao.createAppUser(hagrid, "hagrid123");
    }

    @Test
    @Order(1)
    void create_expense() {
        Expense expense = new Expense(0, 10_000, null,
                OffsetDateTime.now(ZoneId.of("UTC")),
                null, 1, testEmp.getId(), 0);
        dao.createExpense(expense);
        Assertions.assertNotEquals(0, expense.getId());
        testE1 = expense;
    }

    @Test
    @Order(2)
    void get_expense_by_id() {
        int id = testE1.getId();
        Expense expense = dao.getExpenseById(id);
        Assertions.assertEquals(testE1.getEmployeeId(), expense.getEmployeeId());
        Assertions.assertEquals(testE1.getAmountInCents(), expense.getAmountInCents());
    }

    @Test
    @Order(3)
    void update_expense() {
        Expense expense = dao.getExpenseById(testE1.getId());
        expense.setManagerId(testMgr.getId());
        expense.setStatusId(3);
        expense.setMgrReviewedAt(OffsetDateTime.now(ZoneId.of("UTC")));
        expense.setReason("Over Budget");
        dao.updateExpense(expense);
        Expense updatedExpense = dao.getExpenseById(testE1.getId());
        Assertions.assertEquals(testMgr.getId(), updatedExpense.getManagerId());
        Assertions.assertEquals(3, updatedExpense.getStatusId());
        // Assumes previous state has this field null.
        Assertions.assertNotNull(updatedExpense.getReason());
    }

    @Test
    @Order(4)
    void get_all_expenses() {
         testE2 = new Expense(
                 0,
                 20_000,
                 testEmp.getId());
        testE3 = new Expense(
                0,
                30_000,
                testEmp.getId());

        dao.createExpense(testE2);
        dao.createExpense(testE3);

        Set<Expense> allExpenses = dao.getAllExpenses();
        Assertions.assertTrue(allExpenses.size() > 2);
    }

    @Test
    @Order(5)
    void delete_expense_by_id() {
        Assertions.assertTrue(dao.deleteExpenseById(testE1.getId()));
        Assertions.assertTrue(dao.deleteExpenseById(testE2.getId()));
        Assertions.assertTrue(dao.deleteExpenseById(testE3.getId()));
    }

    @AfterAll
    public static void tearDownOnce() {
        // Clean up test resources.
        aDao.deleteAppUserById(testEmp.getId());
        aDao.deleteAppUserById(testMgr.getId());
    }
}
