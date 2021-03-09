package dev.cheun.app;

import dev.cheun.controllers.*;
import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create();

        AppUserController userController = new AppUserController();
        AppUserRoleController roleController = new AppUserRoleController();
        ExpenseController expenseController = new ExpenseController();
        ExpenseStatusController statusController = new ExpenseStatusController();
        LoginController loginController = new LoginController();

        app.get("/users", userController.getAllAppUsersHandler);
        app.get("/users/:id", userController.getAppUserByIdHandler);
        app.get("/user-roles", roleController.getAllRolesHandler);
        app.get("/user-roles/:id", roleController.getRoleByIdHandler);
        app.get("/expense-statuses", statusController.getAllStatusesHandler);
        app.get("/expense-statuses/:id", statusController.getStatusByIdHandler);

        app.get("/users/:id/expenses", expenseController.getExpenseByUserIdHandler);
        app.get("/users/:id/expenses/:eid", expenseController.getExpenseByIdHandler);

        app.post("/users", userController.createAppUserHandler);
        app.post("/users/:id/expenses", expenseController.createExpenseByUserIdHandler);

        app.put("/users/:id/expenses/:eid", expenseController.updateExpenseByUserIdHandler);

        app.delete("/users/:id/expenses/:eid", expenseController.deleteExpenseByUserIdHandler);

        app.post("/users/login", loginController.loginHandler);

        app.start();
    }
}
