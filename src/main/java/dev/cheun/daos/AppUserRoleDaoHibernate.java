package dev.cheun.daos;

import dev.cheun.entities.AppUserRole;
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

public class AppUserRoleDaoHibernate implements AppUserRoleDAO {
    private static final Logger logger = Logger.getLogger(
            AppUserRoleDaoHibernate.class.getName());
    private final SessionFactory sf = HibernateUtil.getSessionFactory();

    @Override
    public AppUserRole createAppUserRole(AppUserRole role) {
        try (Session sess = sf.openSession()) {
            sess.beginTransaction();
            sess.save(role);
            sess.getTransaction().commit();
            logger.info("createAppUserRole: " + role);
            return role;
        } catch (Exception e) {
            AppUtil.logException(logger, e, "createAppUserRole: Unable to create");
            return null;
        }
    }

    @Override
    public Set<AppUserRole> getAllAppUserRoles() {
        try (Session sess = sf.openSession()) {
            CriteriaBuilder cb = sess.getCriteriaBuilder();
            CriteriaQuery<AppUserRole> query = cb.createQuery(AppUserRole.class);
            Root<AppUserRole> root = query.from(AppUserRole.class);
            query.select(root);
            Query<AppUserRole> q = sess.createQuery(query);
            Set<AppUserRole> roles = new HashSet<>(q.getResultList());
            logger.info("getAllAppUserRoles: size=" + roles.size());
            return roles;
        } catch (Exception e) {
            AppUtil.logException(logger, e, "getAllAppUserRoles: Unable to retrieve");
            return null;
        }
    }

    @Override
    public AppUserRole getAppUserRoleById(int id) {
        try (Session sess = sf.openSession()) {
            logger.info("getAppUserRoleById: id=" + id);
            return sess.get(AppUserRole.class, id);
        } catch (Exception e) {
            AppUtil.logException(logger, e, "getAppUserRoleById: Unable to get record with id=" + id);
            return null;
        }
    }

    @Override
    public AppUserRole updateAppUserRole(AppUserRole role) {
        try (Session sess = sf.openSession()) {
            sess.beginTransaction();
            sess.update(role);
            sess.getTransaction().commit();
            logger.info("updateAppUserRole: " + role);
            return role;
        } catch (Exception e) {
            AppUtil.logException(logger, e, "updateAppUserRole: Unable to update");
            return null;
        }
    }

    @Override
    public boolean deleteAppUserRoleById(int id) {
        try (Session sess = sf.openSession()) {
            sess.beginTransaction();
            sess.delete(getAppUserRoleById(id));
            sess.getTransaction().commit();
            logger.info("deleteAppUserRoleById: id=" + id);
            return true;
        } catch (Exception e) {
            AppUtil.logException(logger, e, "deleteAppUserRoleById: Unable to delete record with id=" + id);
            return false;
        }
    }
}
