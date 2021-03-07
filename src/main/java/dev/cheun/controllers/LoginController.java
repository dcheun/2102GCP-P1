package dev.cheun.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.cheun.daos.AppUserDaoPostgres;
import dev.cheun.entities.AppUser;
import dev.cheun.exceptions.NotAuthenticatedException;
import dev.cheun.exceptions.NotFoundException;
import dev.cheun.services.AppUserService;
import dev.cheun.services.AppUserServiceImpl;
import dev.cheun.utils.JwtUtil;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;

public class LoginController {
    private static final AppUserService aServ = new AppUserServiceImpl(new AppUserDaoPostgres());

    public Handler loginHandler = ctx -> {
        String body = ctx.body();
        Gson gson = new Gson();
        // Convert JSON to Java obj.
        JsonObject jobj = gson.fromJson(body, JsonObject.class);
        String username = jobj.get("username").getAsString();
        String password = jobj.get("password").getAsString();
        // Their email is their username.
        AppUser appUser;
        AppUser authUser;
        try {
            appUser = aServ.getAppUserByEmail(username);
            appUser.setPw(password);
            authUser = aServ.authenticate(appUser);
        } catch (NotFoundException | NotAuthenticatedException e) {
            throw new UnauthorizedResponse("Failed to authenticate user");
        }
        String jwt = JwtUtil.generate(authUser.getId(), authUser.getRoleId());
        ctx.cookie("2102GCP_P1_jwt", jwt, 10000);
    };
}
