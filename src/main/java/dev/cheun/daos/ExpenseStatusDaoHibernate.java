package dev.cheun.daos;

import dev.cheun.entities.ExpenseStatus;
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

public class ExpenseStatusDaoHibernate implements ExpenseStatusDAO {
    private static final Logger logger = Logger.getLogger(
            ExpenseStatusDaoHibernate.class.getName());
    private final SessionFactory sf = HibernateUtil.getSessionFactory();

    @Override
    public ExpenseStatus createExpenseStatus(ExpenseStatus expenseStatus) {
        try (Session sess = sf.openSession()) {
            sess.beginTransaction();
            sess.save(expenseStatus);
            sess.getTransaction().commit();
            logger.info("createExpenseStatus: " + expenseStatus);
            return expenseStatus;
        } catch (Exception e) {
            AppUtil.logException(logger, e, "createExpenseStatus: Unable to create");
            return null;
        }
    }

    @Override
    public Set<ExpenseStatus> getAllExpenseStatuses() {
        try (Session sess = sf.openSession()) {
            CriteriaBuilder cb = sess.getCriteriaBuilder();
            CriteriaQuery<ExpenseStatus> query = cb.createQuery(ExpenseStatus.class);
            Root<ExpenseStatus> root = query.from(ExpenseStatus.class);
            query.select(root);
            Query<ExpenseStatus> q = sess.createQuery(query);
            Set<ExpenseStatus> expenseStatuses = new HashSet<>(q.getResultList());
            logger.info("getAllExpenseStatuses: size=" + expenseStatuses.size());
            return expenseStatuses;
        } catch (Exception e) {
            AppUtil.logException(logger, e, "getAllExpenseStatus: Unable to retrieve");
            return null;
        }
    }

    @Override
    public ExpenseStatus getExpenseStatusById(int id) {
        try (Session sess = sf.openSession()) {
            logger.info("getExpenseStatusById: id=" + id);
            return sess.get(ExpenseStatus.class, id);
        } catch (Exception e) {
            AppUtil.logException(logger, e,
                    "getExpenseStatusById: Unable to get record with id=" + id);
            return null;
        }
    }

    @Override
    public ExpenseStatus updateExpenseStatus(ExpenseStatus expenseStatus) {
        try (Session sess = sf.openSession()) {
            sess.beginTransaction();
            sess.update(expenseStatus);
            sess.getTransaction().commit();
            logger.info("updateExpenseStatus: " + expenseStatus);
            return expenseStatus;
        } catch (Exception e) {
            AppUtil.logException(logger, e, "updateExpenseStatus: Unable to update");
            return null;
        }
    }

    @Override
    public boolean deleteExpenseStatusById(int id) {
        try (Session sess = sf.openSession()) {
            sess.beginTransaction();
            sess.delete(getExpenseStatusById(id));
            sess.getTransaction().commit();
            logger.info("deleteExpenseStatusById: id=" + id);
            return true;
        } catch (Exception e) {
            AppUtil.logException(logger, e,
                    "deleteExpenseStatusById: Unable to delete record with id=" + id);
            return false;
        }
    }
}
