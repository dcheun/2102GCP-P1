package dev.cheun.daotests;

import dev.cheun.daos.AppUserDAO;
import dev.cheun.daos.AppUserDaoPostgres;
import dev.cheun.entities.AppUser;
import dev.cheun.exceptions.NotAuthenticatedException;
import org.junit.jupiter.api.*;

import java.util.Set;

// Junit tests not guaranteed to run in order unless you tell it to.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppUserDaoTests {
    private static AppUserDAO dao = new AppUserDaoPostgres();
    private static AppUser testA1 = null;
    private static AppUser testA2 = null;
    private static AppUser testA3 = null;

    @Test
    @Order(1)
    void create_app_user() {
        // ID of zero means that object has not been saved/persisted somewhere.
        AppUser ron = new AppUser(0,"Ron","Weasley","rweasley@hogwarts.edu",1);
        dao.createAppUser(ron, "weasley123");
        testA1 = ron;
        Assertions.assertNotEquals(0, testA1.getId());
    }

    @Test
    @Order(2)
    void get_app_user_by_id(){
        int id = testA1.getId();
        AppUser appUser = dao.getAppUserById(id);
        Assertions.assertEquals(testA1.getEmail(), appUser.getEmail());
    }

    @Test
    @Order(3)
    void authenticate_success() {
        AppUser appUser = dao.authenticate(testA1, "weasley123");
        Assertions.assertEquals(testA1.getId(), appUser.getId());
    }

    // Test authenticate failure (eg: bad password) scenario.
    // Should throw NotAuthenticatedException.
    @Test
    @Order(4)
    void authenticate_fail() {
        Exception e = Assertions.assertThrows(NotAuthenticatedException.class, () -> {
            dao.authenticate(testA1, "WrongPassword");
        });
        String expectedMessage = "Failed to authenticate user";
        Assertions.assertTrue(e.getMessage().contains(expectedMessage));
    }

    @Test
    @Order(5)
    void update_app_user() {
        AppUser appUser = dao.getAppUserById(testA1.getId());
        appUser.setFname("Neville");
        appUser.setLname("Longbottom");
        appUser.setEmail("nlongbottom@hogwarts.edu");
        dao.updateAppUser(appUser);
        AppUser updatedAppUser = dao.getAppUserById(testA1.getId());
        Assertions.assertEquals("Neville", updatedAppUser.getFname());
        Assertions.assertEquals("Longbottom", updatedAppUser.getLname());
        Assertions.assertEquals("nlongbottom@hogwarts.edu", updatedAppUser.getEmail());
    }

    @Test
    @Order(6)
    void get_all_app_users() {
        testA2 = new AppUser(0, "Rubeus", "Hagrid", "rhagrid@hogwarts.edu", 2);
        testA3 = new AppUser(0, "Luna", "Lovegood", "llovegood@hogwarts.edu", 1);

        dao.createAppUser(testA2, "hagrid123");
        dao.createAppUser(testA3, "lovegood123");

        Set<AppUser> allAppUsers = dao.getAllAppUsers();
        Assertions.assertTrue(allAppUsers.size() > 2);
    }

    @Test
    @Order(7)
    void delete_app_user_by_id() {
        int id = testA1.getId();
        boolean result = dao.deleteAppUserById(id);
        Assertions.assertTrue(result);

        id = testA2.getId();
        result = dao.deleteAppUserById(id);
        Assertions.assertTrue(result);

        id = testA3.getId();
        result = dao.deleteAppUserById(id);
        Assertions.assertTrue(result);
    }

}
