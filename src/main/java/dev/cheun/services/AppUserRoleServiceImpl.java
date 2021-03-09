package dev.cheun.services;

import dev.cheun.daos.AppUserRoleDAO;
import dev.cheun.entities.AppUserRole;

import java.util.Set;

public class AppUserRoleServiceImpl implements AppUserRoleService {

    private final AppUserRoleDAO dao;

    public AppUserRoleServiceImpl(AppUserRoleDAO dao) {
        this.dao = dao;
    }

    @Override
    public AppUserRole createRole(AppUserRole role) {
        return this.dao.createAppUserRole(role);
    }

    @Override
    public Set<AppUserRole> getAllRoles() {
        return this.dao.getAllAppUserRoles();
    }

    @Override
    public AppUserRole getRoleById(int id) {
        return this.dao.getAppUserRoleById(id);
    }

    @Override
    public AppUserRole updateRole(AppUserRole role) {
        return this.dao.updateAppUserRole(role);
    }

    @Override
    public boolean deleteRole(int id) {
        return this.dao.deleteAppUserRoleById(id);
    }
}
