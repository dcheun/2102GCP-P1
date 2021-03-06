package dev.cheun.services;

import dev.cheun.entities.AppUser;

import java.util.Set;

public interface AppUserService {
    // CREATE
    AppUser registerAppUser(AppUser appUser, String pw);

    // READ
    Set<AppUser> getAllAppUsers();
    Set<AppUser> getAppUserByName(String fname, String lname);
    AppUser getAppUserById(int id);
    AppUser getEmployeeById(int employeeId);
    AppUser getManagerById(int managerId);
    AppUser getAppUserByEmail(String email);
    AppUser authenticate(AppUser appUser, String pw);

    // UPDATE
    AppUser updateAppUser(AppUser appUser);

    // DELETE
    boolean deleteAppUserById(int id);
}
