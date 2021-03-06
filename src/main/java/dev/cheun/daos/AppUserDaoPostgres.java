package dev.cheun.daos;

import dev.cheun.entities.AppUser;
import dev.cheun.exceptions.NotAuthenticatedException;
import dev.cheun.exceptions.NotFoundException;
import dev.cheun.utils.ConnectionUtil;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class AppUserDaoPostgres implements AppUserDAO {

    private static final Logger logger = Logger.getLogger(AppUserDaoPostgres.class.getName());

    @Override
    public AppUser createAppUser(AppUser appUser, String pw) {
        // Try with resources syntax.
        // Java will always close the object to prevent resource leaks.
        try (Connection conn = ConnectionUtil.createConnection()) {
            String sql = "INSERT INTO app_user " +
                    "(fname, lname, email, user_role, pw) " +
                    "VALUES (?, ?, ?, ?, " +
                    "crypt(?, gen_salt('bf')))";
            PreparedStatement ps = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);
            // Set the field params.
            ps.setString(1, appUser.getFname());
            ps.setString(2, appUser.getLname());
            ps.setString(3, appUser.getEmail());
            ps.setInt(4, appUser.getUserRole());
            ps.setString(5, pw);
            ps.execute();
            // Return the val of the generated keys.
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int key = rs.getInt("id");
            appUser.setId(key);
            logger.info("createAppUser: " + appUser);
            return appUser;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("createAppUser: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Set<AppUser> getAllAppUsers() {
        try (Connection conn = ConnectionUtil.createConnection()) {
            String sql = "SELECT * FROM app_user";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            Set<AppUser> appUsers = new HashSet<>();
            // While there exist records, create a new instance and store
            // the information into the new instance.
            while (rs.next()) {
                AppUser appUser = new AppUser();
                appUser.setId(rs.getInt("id"));
                appUser.setFname(rs.getString("fname"));
                appUser.setLname(rs.getString("lname"));
                appUser.setEmail(rs.getString("email"));
                appUser.setUserRole(rs.getInt("user_role"));
                appUsers.add(appUser);
            }
            logger.info("getAllAppUsers: size=" + appUsers.size());
            return appUsers;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("getAllAppUsers: " + e.getMessage());
            return null;
        }
    }

    @Override
    public AppUser getAppUserById(int id) {
        try (Connection conn = ConnectionUtil.createConnection()) {
            String sql = "SELECT * FROM app_user WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotFoundException("No such user exists");
            }
            AppUser appUser = new AppUser();
            appUser.setId(rs.getInt("id"));
            appUser.setFname(rs.getString("fname"));
            appUser.setLname(rs.getString("lname"));
            appUser.setEmail(rs.getString("email"));
            appUser.setUserRole(rs.getInt("user_role"));
            logger.info("getAppUserById: id=" + id);
            return appUser;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("getAppUserById: Unable to get record with id=" + id);
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public AppUser authenticate(AppUser appUser, String pw) {
        try(Connection conn = ConnectionUtil.createConnection()) {
            String sql = "SELECT * FROM app_user WHERE email = ? and " +
                    "pw = crypt(?, pw)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, appUser.getEmail());
            ps.setString(2, pw);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotAuthenticatedException("Failed to authenticate user");
            }
            AppUser authedUser = new AppUser();
            authedUser.setId(rs.getInt("id"));
            authedUser.setFname(rs.getString("fname"));
            authedUser.setLname(rs.getString("lname"));
            authedUser.setEmail(rs.getString("email"));
            authedUser.setUserRole(rs.getInt("user_role"));
            logger.info("authenticate: " + appUser);
            return authedUser;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("authenticate: " + e.getMessage());
            return null;
        }
    }

    @Override
    public AppUser updateAppUser(AppUser appUser) {
        try (Connection conn = ConnectionUtil.createConnection()) {
            String sql = "UPDATE app_user " +
                    "SET fname = ?, lname = ?, email = ?, user_role = ? " +
                    "WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, appUser.getFname());
            ps.setString(2, appUser.getLname());
            ps.setString(3, appUser.getEmail());
            ps.setInt(4, appUser.getUserRole());
            ps.setInt(5, appUser.getId());
            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                throw new NotFoundException("No such user exists");
            }
            logger.info("updateAppUser: " + appUser);
            return appUser;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("updateAppUser: Unable to update: " + appUser);
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean deleteAppUserById(int id) {
        try (Connection conn = ConnectionUtil.createConnection()) {
            String sql = "DELETE FROM app_user WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                throw new NotFoundException("Unable to delete: No such user exists");
            }
            logger.info("deleteAppUserById: id=" + id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("deleteAppUserById: Unable to delete record with id=" + id);
            logger.error(e.getMessage());
            return false;
        }
    }
}
