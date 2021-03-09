package dev.cheun.controllers;

import com.google.gson.Gson;
import dev.cheun.daos.AppUserRoleDaoHibernate;
import dev.cheun.entities.AppUserRole;
import dev.cheun.exceptions.NotFoundException;
import dev.cheun.services.AppUserRoleService;
import dev.cheun.services.AppUserRoleServiceImpl;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;

import java.util.Set;

public class AppUserRoleController {

    private final AppUserRoleService serv = new AppUserRoleServiceImpl(
            new AppUserRoleDaoHibernate());

    public Handler getAllRolesHandler = ctx -> {
        Set<AppUserRole> allRoles = this.serv.getAllRoles();
        Gson gson = new Gson();
        ctx.result(gson.toJson(allRoles));
    };

    public Handler getRoleByIdHandler = ctx -> {
        int id = Integer.parseInt(ctx.pathParam("id"));
        AppUserRole role;
        try {
            role = this.serv.getRoleById(id);
        } catch (NotFoundException e) {
            throw new NotFoundResponse(e.getMessage());
        }
        Gson gson = new Gson();
        ctx.result(gson.toJson(role));
    };
}
