package dev.cheun.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.cheun.daos.AppUserDaoHibernate;
import dev.cheun.entities.AppUser;
import dev.cheun.exceptions.NotFoundException;
import dev.cheun.services.AppUserService;
import dev.cheun.services.AppUserServiceImpl;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;

import java.util.Set;

// All logic in controller should deal with the API.
// Controller should call services to perform the actions.
public class AppUserController {
    private final AppUserService appUserService = new AppUserServiceImpl(
            new AppUserDaoHibernate());

    public Handler getAllAppUsersHandler = ctx -> {
        Set<AppUser> allAppUsers = this.appUserService.getAllAppUsers();
        Gson gson = new Gson();
        ctx.result(gson.toJson(allAppUsers));
    };

    public Handler getAppUserByIdHandler = ctx -> {
        int id = Integer.parseInt(ctx.pathParam("id"));
        AppUser appUser;
        try {
            appUser = this.appUserService.getAppUserById(id);
        } catch (NotFoundException e) {
            throw new NotFoundResponse(e.getMessage());
        }
        Gson gson = new Gson();
        ctx.result(gson.toJson(appUser));
    };

    // The body will contain a JSON with the create info.
    public Handler createAppUserHandler = ctx -> {
        String body = ctx.body();
        Gson gson = new Gson();
        // Convert JSON to Java object.
        // Intermediary use of JsonObject to extract just the pw.
        JsonObject jobj = gson.fromJson(body, JsonObject.class);
        String pw = jobj.get("pw").getAsString();
        AppUser appUser = gson.fromJson(body, AppUser.class);
        appUser.setPw(pw);
        AppUser newUser = this.appUserService.registerAppUser(appUser);
        if (newUser == null) {
            ctx.status(500).result("Unable to create user");
            return;
        }
        ctx.status(201).result(gson.toJson(newUser));
    };
}
