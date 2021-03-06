package dev.cheun.app;

import dev.cheun.controllers.AppUserController;
import dev.cheun.controllers.ExpenseController;
import dev.cheun.controllers.LoginController;
import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create();

        AppUserController userController = new AppUserController();
        LoginController loginController = new LoginController();
        ExpenseController expenseController = new ExpenseController();

        app.get("/users", userController.getAllAppUsersHandler);
        app.get("/users/:id", userController.getAppUserByIdHandler);

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
