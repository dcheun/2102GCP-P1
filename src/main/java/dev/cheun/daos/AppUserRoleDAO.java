package dev.cheun.daos;

import dev.cheun.entities.AppUserRole;

import java.util.Set;

public interface AppUserRoleDAO {
    // Create
    AppUserRole createAppUserRole(AppUserRole role);

    // Read
    Set<AppUserRole> getAllAppUserRoles();
    AppUserRole getAppUserRoleById(int id);

    // Update
    AppUserRole updateAppUserRole(AppUserRole role);

    // Delete
    boolean deleteAppUserRoleById(int id);
}
