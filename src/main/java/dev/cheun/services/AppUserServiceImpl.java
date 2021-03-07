package dev.cheun.services;

import dev.cheun.daos.AppUserDAO;
import dev.cheun.entities.AppUser;
import dev.cheun.exceptions.NotFoundException;

import java.util.HashSet;
import java.util.Set;

public class AppUserServiceImpl implements AppUserService {

    // Service will need a DAO to get and save records.
    private final AppUserDAO dao;

    // Dependency injection.
    // A service is created by passing in the dependencies it needs.
    public AppUserServiceImpl(AppUserDAO dao) {
        this.dao = dao;
    }

    @Override
    public AppUser registerAppUser(AppUser appUser) {
        return this.dao.createAppUser(appUser);
    }

    @Override
    public Set<AppUser> getAllAppUsers() {
        return this.dao.getAllAppUsers();
    }

    @Override
    public Set<AppUser> getAppUserByName(String fname, String lname) {
        Set<AppUser> allAppUsers = this.getAllAppUsers();
        Set<AppUser> selectedAppUsers = new HashSet<>();
        for (AppUser u : allAppUsers) {
            if (u.getFname().contains(fname) && u.getLname().contains(lname)) {
                selectedAppUsers.add(u);
            }
        }
        return selectedAppUsers;
    }

    @Override
    public AppUser getAppUserById(int id) {
        return this.dao.getAppUserById(id);
    }

    @Override
    public AppUser getEmployeeById(int employeeId) {
        AppUser appUser = getAppUserById(employeeId);
        if (appUser.getRoleId() == 1) {
            return appUser;
        }
        throw new NotFoundException("No such employee exists");
    }

    @Override
    public AppUser getManagerById(int managerId) {
        AppUser appUser = getAppUserById(managerId);
        if (appUser.getRoleId() == 2) {
            return appUser;
        }
        throw new NotFoundException("No such manager exists");
    }

    @Override
    public AppUser getAppUserByEmail(String email) {
        Set<AppUser> allAppUsers = this.getAllAppUsers();
        for (AppUser u : allAppUsers) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        throw new NotFoundException("No user with that email exists");
    }

    @Override
    public AppUser authenticate(AppUser appUser) {
        return this.dao.authenticate(appUser);
    }

    @Override
    public AppUser updateAppUser(AppUser appUser) {
        return this.dao.updateAppUser(appUser);
    }

    @Override
    public boolean deleteAppUserById(int id) {
        return this.dao.deleteAppUserById(id);
    }
}
