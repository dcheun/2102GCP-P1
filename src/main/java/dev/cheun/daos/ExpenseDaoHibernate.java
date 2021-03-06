package dev.cheun.daos;

import dev.cheun.entities.Expense;
import dev.cheun.exceptions.NotFoundException;
import dev.cheun.utils.AppUtil;
import dev.cheun.utils.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.Set;

public class ExpenseDaoHibernate implements ExpenseDAO {

    private static final Logger logger = Logger.getLogger(
            ExpenseDaoHibernate.class.getName());
    private final SessionFactory sf = HibernateUtil.getSessionFactory();

    @Override
    public Expense createExpense(Expense expense) {
        try (Session sess = sf.openSession()) {
            sess.beginTransaction();
            sess.save(expense);
            sess.getTransaction().commit();
            logger.info("createExpense: " + expense);
            return expense;
        } catch (Exception e) {
            AppUtil.logException(logger, e, "createExpense: Unable to create");
            return null;
        }
    }

    @Override
    public Set<Expense> getAllExpenses() {
        try (Session sess = sf.openSession()) {
            CriteriaBuilder cb = sess.getCriteriaBuilder();
            CriteriaQuery<Expense> query = cb.createQuery(Expense.class);
            Root<Expense> root = query.from(Expense.class);
            query.select(root);
            Query<Expense> q = sess.createQuery(query);
            Set<Expense> expenses = new HashSet<>(q.getResultList());
            logger.info("getAllExpenses: size=" + expenses.size());
            return expenses;
        } catch (Exception e) {
            AppUtil.logException(logger, e, "getAllExpenses: Unable to retrieve");
            return null;
        }
    }

    @Override
    public Expense getExpenseById(int id) {
        try (Session sess = sf.openSession()) {
            Expense expense = sess.get(Expense.class, id);
            // null means no record was found.
            if (expense == null) {
                throw new NotFoundException("No such expense exists");
            }
            logger.info("getExpenseById: id=" + id);
            return expense;
        } catch (NotFoundException e) {
            logger.warn("getExpenseById: Not found: id=" + id);
            throw e;
        } catch (Exception e) {
            AppUtil.logException(logger, e,
                    "getExpenseById: Unable to get record with id=" + id);
            return null;
        }
    }

    @Override
    public Expense updateExpense(Expense expense) {
        try (Session sess = sf.openSession()) {
            sess.beginTransaction();
            // Check if record exists.
            // Throws a NotFoundException if not found.
            getExpenseById(expense.getId());
            sess.update(expense);
            sess.getTransaction().commit();
            logger.info("updateExpense: " + expense);
            return expense;
        } catch (NotFoundException e) {
            logger.warn("updateExpense: Not found: id=" + expense.getId());
            throw e;
        } catch (Exception e) {
            AppUtil.logException(logger, e, "updateExpense: Unable to update");
            return null;
        }
    }

    @Override
    public boolean deleteExpenseById(int id) {
        try (Session sess = sf.openSession()) {
            sess.beginTransaction();
            // getExpenseById will throw NotFoundException if id doesn't exist.
            sess.delete(getExpenseById(id));
            sess.getTransaction().commit();
            logger.info("deleteExpenseById: id=" + id);
            return true;
        } catch (NotFoundException e) {
            logger.warn("deleteExpenseById: Not found: id=" + id);
            throw e;
        } catch (Exception e) {
            AppUtil.logException(logger, e,
                    "deleteExpenseById: Unable to delete user with id=" + id);
            return false;
        }
    }
}
