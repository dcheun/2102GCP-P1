package dev.cheun.daos;

import dev.cheun.entities.AppUser;
import dev.cheun.exceptions.NotAuthenticatedException;
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
import java.util.List;
import java.util.Set;

public class AppUserDaoHibernate implements AppUserDAO {

    private static final Logger logger = Logger.getLogger(
            AppUserDaoHibernate.class.getName());
    private final SessionFactory sf = HibernateUtil.getSessionFactory();

    @Override
    public AppUser createAppUser(AppUser appUser) {
        // Try with resources syntax.
        // Java will always close the object to prevent resource leaks.
        try (Session sess = sf.openSession()) {
            sess.beginTransaction();
            sess.save(appUser); // Changes the object to have the new id.
            sess.getTransaction().commit();
            logger.info("createAppUser: " + appUser);
            return appUser;
        } catch (Exception e) {
            AppUtil.logException(logger, e, "createAppUser: Unable to create");
            return null;
        }
    }

    @Override
    public Set<AppUser> getAllAppUsers() {
        try (Session sess = sf.openSession()) {
            CriteriaBuilder builder = sess.getCriteriaBuilder();
            CriteriaQuery<AppUser> query = builder.createQuery(AppUser.class);
            Root<AppUser> root = query.from(AppUser.class);
            query.select(root);
            Query<AppUser> q = sess.createQuery(query);
            Set<AppUser> users = new HashSet<>(q.getResultList());
            logger.info("getAllAppUsers: size=" + users.size());
            return users;
        } catch (Exception e) {
            AppUtil.logException(logger, e, "getAllAppUsers: Unable to retrieve");
            return null;
        }
    }

    @Override
    public AppUser getAppUserById(int id) {
        try (Session sess = sf.openSession()) {
            logger.info("getAppUserById: id=" + id);
            return sess.get(AppUser.class, id);
        } catch (Exception e) {
            AppUtil.logException(logger, e,
                    "getAppUserById: Unable to get record with id=" + id);
            return null;
        }
    }

    @Override
    public AppUser authenticate(AppUser appUser) {
        try (Session sess = sf.openSession()) {
            String sql = "SELECT pw = crypt('" + appUser.getPw() +
                    "', pw) FROM app_user WHERE id = " + appUser.getId();
            Query query = sess.createSQLQuery(sql);
            List<Boolean> row = query.list();
            if (!row.get(0)) {
                logger.warn("authenticate: Failed to authenticate: " + appUser);
                throw new NotAuthenticatedException("Failed to authenticate user");
            }
            logger.info("authenticate: " + appUser);
            return appUser;
        } catch (NotAuthenticatedException e) {
            throw e;
        } catch (Exception e) {
            AppUtil.logException(logger, e,
                    "authenticate: Unable to authenticate user");
            return null;
        }
    }

    @Override
    public AppUser updateAppUser(AppUser appUser) {
        try (Session sess = sf.openSession()) {
            sess.beginTransaction();
            sess.update(appUser);
            sess.getTransaction().commit();
            logger.info("updateAppUser: " + appUser);
            return appUser;
        } catch (Exception e) {
            AppUtil.logException(logger, e, "updateAppUser: Unable to update");
            return null;
        }
    }

    @Override
    public boolean deleteAppUserById(int id) {
        try (Session sess = sf.openSession()) {
            sess.beginTransaction();
            sess.delete(getAppUserById(id));
            sess.getTransaction().commit();
            logger.info("deleteAppUserById: id=" + id);
            return true;
        } catch (Exception e) {
            AppUtil.logException(logger, e,
                    "deleteAppUserById: Unable to delete user with id=" + id);
            return false;
        }
    }
}
