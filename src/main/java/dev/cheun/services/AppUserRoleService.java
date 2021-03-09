package dev.cheun.services;

import dev.cheun.entities.AppUserRole;

import java.util.Set;

public interface AppUserRoleService {
    // Create
    AppUserRole createRole(AppUserRole role);

    // Read
    Set<AppUserRole> getAllRoles();

    AppUserRole getRoleById(int id);

    // Update
    AppUserRole updateRole(AppUserRole role);

    // Delete
    boolean deleteRole(int id);
}
