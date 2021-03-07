package dev.cheun.daos;

import dev.cheun.entities.AppUser;

import java.util.Set;

public interface AppUserDAO {
    // Create
    AppUser createAppUser(AppUser appUser);

    // Read
    Set<AppUser> getAllAppUsers();
    AppUser getAppUserById(int id);
    AppUser authenticate(AppUser appUser);

    // Update
    AppUser updateAppUser(AppUser appUser);

    // Delete
    boolean deleteAppUserById(int id);
}
