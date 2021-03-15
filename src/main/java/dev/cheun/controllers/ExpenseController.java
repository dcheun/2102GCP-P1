package dev.cheun.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import dev.cheun.daos.ExpenseDaoHibernate;
import dev.cheun.entities.Expense;
import dev.cheun.exceptions.NotFoundException;
import dev.cheun.services.ExpenseService;
import dev.cheun.services.ExpenseServiceImpl;
import dev.cheun.utils.DateTimeUtil;
import dev.cheun.utils.JwtUtil;
import io.javalin.http.*;

import java.util.Set;

public class ExpenseController {

    private final ExpenseService eServ = new ExpenseServiceImpl(
            new ExpenseDaoHibernate());

    public Handler getExpenseByUserIdHandler = ctx -> {
        int id = Integer.parseInt(ctx.pathParam("id"));
        DecodedJWT jwt = extractAndCheckJWT(ctx);
        int authUserId = jwt.getClaim("sub").asInt();
        int authRoleId = jwt.getClaim("role").asInt();
        Set<Expense> expenseSet;
        // pathParm id of 0 -> get all expense (must be mgr)
        if (id == 0) {
            // This is a getAll request - must be done by manager.
            if(authRoleId != 2) {
                throw new ForbiddenResponse("Access denied");
            }
            expenseSet = eServ.getAllExpenses();
        } else {
            // Check if employee id matches.
            if(authRoleId == 1 && authUserId != id) {
                throw new ForbiddenResponse("Access denied");
            }
            expenseSet = eServ.getExpensesByEmployeeId(id);
        }
        ctx.result(new Gson().toJson(expenseSet));
    };

    public Handler getExpenseByIdHandler = ctx -> {
        int id = Integer.parseInt(ctx.pathParam("id"));
        int eid = Integer.parseInt(ctx.pathParam("eid"));
        DecodedJWT jwt = extractAndCheckJWT(ctx);
        int authUserId = jwt.getClaim("sub").asInt();
        int authRoleId = jwt.getClaim("role").asInt();
        // Check if employee id matches.
        if(authRoleId == 1 && authUserId != id) {
            throw new ForbiddenResponse("Access denied");
        }
        Expense expense;
        try {
            expense = eServ.getExpenseById(eid);
        } catch (NotFoundException e) {
            throw new NotFoundResponse(e.getMessage());
        }
        if (authRoleId == 1 && expense.getEmployeeId() != id) {
            throw new ForbiddenResponse("Access denied");
        }
        ctx.result(new Gson().toJson(expense));
    };

    public Handler createExpenseByUserIdHandler = ctx -> {
        int id = Integer.parseInt(ctx.pathParam("id"));
        DecodedJWT jwt = extractAndCheckJWT(ctx);
        int authUserId = jwt.getClaim("sub").asInt();
        int authRoleId = jwt.getClaim("role").asInt();
        // Check if employee id matches.
        if (authRoleId == 1 && authUserId != id) {
            throw new ForbiddenResponse("Access denied");
        }
        String body = ctx.body();
        Gson gson = new Gson();
        Expense expense = gson.fromJson(body, Expense.class);
        expense.setEmployeeId(id);
        expense.setSubmittedAt(DateTimeUtil.getOffsetDateTimeUtcNow());
        expense.setStatusId(1);
        Expense newExpense = eServ.createExpense(expense);
        ctx.status(201).result(gson.toJson(newExpense));
    };

    public Handler updateExpenseByUserIdHandler = ctx -> {
        int id = Integer.parseInt(ctx.pathParam("id"));
        int eid = Integer.parseInt(ctx.pathParam("eid"));
        DecodedJWT jwt = extractAndCheckJWT(ctx);
        int authUserId = jwt.getClaim("sub").asInt();
        int authRoleId = jwt.getClaim("role").asInt();

        String body = ctx.body();
        Gson gson = new Gson();
        Expense expense = gson.fromJson(body, Expense.class);
        expense.setId(eid); // Often redundant, but the path param takes precedent.
        expense.setEmployeeId(id);

        // Check existing expense.
        Expense currExpense;
        try {
            currExpense = eServ.getExpenseById(eid);
        } catch (NotFoundException e) {
            throw new NotFoundResponse(e.getMessage());
        }

        // Check employee requests.
        if (authRoleId == 1) {
            if (authUserId != id) {
                throw new ForbiddenResponse("Access denied");
            }
            // Employee cannot update non-pending expenses.
            if (currExpense.getStatusId() > 1) {
                throw new BadRequestResponse("Cannot update non-pending expenses");
            }
            // Set status to pending (in case not specified in body).
            expense.setStatusId(1);
        }
        // Check mgr requests.
        if (authRoleId == 2) {
            expense.setManagerId(authUserId);
            expense.setMgrReviewedAt(DateTimeUtil.getOffsetDateTimeUtcNow());
        }

        // Fill in the rest of expense.
        if (expense.getStatusId() == 0) {
            expense.setStatusId(currExpense.getStatusId());
        }
        if (expense.getAmountInCents() == 0) {
            expense.setAmountInCents(currExpense.getAmountInCents());
        }
        if (expense.getReason() == null) {
            expense.setReason(currExpense.getReason());
        }
        if (expense.getSubmittedAt() == null) {
            expense.setSubmittedAt(currExpense.getSubmittedAt());
        }
        if (expense.getMgrReviewedAt() == null) {
            expense.setMgrReviewedAt(currExpense.getMgrReviewedAt());
        }
        if (expense.getManagerId() == null) {
            expense.setManagerId(currExpense.getManagerId());
        }

        Expense updatedExpense = eServ.updateExpense(expense);
        ctx.result(gson.toJson(updatedExpense));
    };


    public Handler deleteExpenseByUserIdHandler = ctx -> {
        int id = Integer.parseInt(ctx.pathParam("id"));
        int eid = Integer.parseInt(ctx.pathParam("eid"));
        DecodedJWT jwt = extractAndCheckJWT(ctx);
        int authUserId = jwt.getClaim("sub").asInt();
        int authRoleId = jwt.getClaim("role").asInt();
        // Check if employee id matches.
        if(authRoleId == 1 && authUserId != id) {
            throw new ForbiddenResponse("Access denied");
        }
        Expense expense;
        try {
            expense = eServ.getExpenseById(eid);
        } catch (NotFoundException e) {
            throw new NotFoundResponse(e.getMessage());
        }
        if (authRoleId == 1) {
            if (expense.getEmployeeId() != id) {
                throw new ForbiddenResponse("Access denied");
            }
            if (expense.getStatusId() > 1) {
                throw new BadRequestResponse("Cannot delete non-pending expenses");
            }
        }
        boolean deleted = eServ.deleteExpenseById(eid);
        if (deleted) {
            ctx.result("Expense with id " + eid + " was deleted");
        } else {
            ctx.status(500).result("Unable to delete expense with id " + id);
        }
    };

    private DecodedJWT extractAndCheckJWT(Context ctx) {
        String token;
        // Token needs to be in the following form in the header:
        // {"Authorization": "Bearer encoded_jwt"}
        try {
            token = ctx.header("Authorization").split(" ")[1];
        } catch (Exception e) {
            throw new UnauthorizedResponse("Bad token");
        }
        // Verify token.
        DecodedJWT jwt;
        try {
            jwt = JwtUtil.isValidJWT(token);
        } catch (Exception e) {
            throw new UnauthorizedResponse("Bad token");
        }
        return jwt;
    }
}
