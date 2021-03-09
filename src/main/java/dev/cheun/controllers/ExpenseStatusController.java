package dev.cheun.controllers;

import com.google.gson.Gson;
import dev.cheun.daos.ExpenseStatusDaoHibernate;
import dev.cheun.entities.ExpenseStatus;
import dev.cheun.exceptions.NotFoundException;
import dev.cheun.services.ExpenseStatusService;
import dev.cheun.services.ExpenseStatusServiceImpl;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;

import java.util.Set;

public class ExpenseStatusController {

    private final ExpenseStatusService serv = new ExpenseStatusServiceImpl(
            new ExpenseStatusDaoHibernate());

    public Handler getAllStatusesHandler = ctx -> {
        Set<ExpenseStatus> allStatuses = this.serv.getAllStatuses();
        Gson gson = new Gson();
        ctx.result(gson.toJson(allStatuses));
    };

    public Handler getStatusByIdHandler = ctx -> {
        int id = Integer.parseInt(ctx.pathParam("id"));
        ExpenseStatus status;
        try {
            status = this.serv.getStatusById(id);
        } catch (NotFoundException e) {
            throw new NotFoundResponse(e.getMessage());
        }
        Gson gson = new Gson();
        ctx.result(gson.toJson(status));
    };
}
