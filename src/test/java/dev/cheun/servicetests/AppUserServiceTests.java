package dev.cheun.servicetests;

import dev.cheun.daos.AppUserDaoPostgres;
import dev.cheun.entities.AppUser;
import dev.cheun.exceptions.NotFoundException;
import dev.cheun.services.AppUserService;
import dev.cheun.services.AppUserServiceImpl;
import org.junit.jupiter.api.*;

import java.util.Set;

// Business Logic
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppUserServiceTests {
    private static final AppUserService serv = new AppUserServiceImpl(new AppUserDaoPostgres());
    private static AppUser testA1 = null;
    private static AppUser testA2 = null;

    @Test
    @Order(1)
    void register_app_user() {
        testA1 = new AppUser(0, "Ron", "Weasley", "rweasley@hogwarts.edu", 1);
        testA2 = new AppUser(0, "Rubeus", "Hagrid", "rhagrid@hogwarts.edu", 2);

        serv.registerAppUser(testA1, "weasley123");
        serv.registerAppUser(testA2, "hagrid123");

        Assertions.assertNotEquals(0, testA1.getId());
        Assertions.assertNotNull(testA1.getEmail());
        Assertions.assertNotEquals(0, testA2.getId());
        Assertions.assertNotNull(testA2.getEmail());
    }

    @Test
    @Order(2)
    void get_app_user_by_name() {
        Set<AppUser> appUsers = serv.getAppUserByName("Ron", "Wea");
        AppUser foundUser = null;
        for (AppUser u : appUsers) {
            if (u.getId() == testA1.getId()) {
                foundUser = u;
                break;
            }
        }
        Assertions.assertNotNull(foundUser);
    }

    @Test
    @Order(3)
    void get_employee_by_id() {
        AppUser appUser = serv.getEmployeeById(testA1.getId());
        Assertions.assertEquals(testA1.getId(), appUser.getId());

        Exception e = Assertions.assertThrows(NotFoundException.class, () -> {
            serv.getEmployeeById(testA2.getId());
        });
        String expectedMessage = "No such employee exists";
        Assertions.assertTrue(e.getMessage().contains(expectedMessage));
    }

    @Test
    @Order(4)
    void get_manager_by_id() {
        AppUser appUser = serv.getManagerById(testA2.getId());
        Assertions.assertEquals(testA2.getId(), appUser.getId());

        Exception e = Assertions.assertThrows(NotFoundException.class, () -> {
            serv.getManagerById(testA1.getId());
        });
        String expectedMessage = "No such manager exists";
        Assertions.assertTrue(e.getMessage().contains(expectedMessage));
    }

    @Test
    @Order(5)
    void delete_app_user_by_id() {
        Assertions.assertTrue(serv.deleteAppUserById(testA1.getId()));
        Assertions.assertTrue(serv.deleteAppUserById(testA2.getId()));
    }
}
