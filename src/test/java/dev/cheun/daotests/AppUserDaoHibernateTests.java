package dev.cheun.daotests;

import dev.cheun.daos.AppUserDAO;
import dev.cheun.daos.AppUserDaoHibernate;
import dev.cheun.entities.AppUser;
import dev.cheun.exceptions.NotAuthenticatedException;
import org.junit.jupiter.api.*;

import java.util.Set;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppUserDaoHibernateTests {
    private static final AppUserDAO dao = new AppUserDaoHibernate();
    private static AppUser testA1 = null;
    private static AppUser testA2 = null;
    private static AppUser testA3 = null;

    @Test
    @Order(1)
    void create_app_user() {
        AppUser ron = new AppUser(
                0,
                "Ron",
                "Weasley",
                "rweasley@hogwarts.edu",
                1,
                "weasley123");
        dao.createAppUser(ron);
        Assertions.assertNotEquals(0, ron.getId());
        testA1 = ron;
    }

    @Test
    @Order(2)
    void get_user_by_id() {
        AppUser appUser = dao.getAppUserById(testA1.getId());
        Assertions.assertEquals(testA1.getEmail(), appUser.getEmail());
    }

    @Test
    @Order(3)
    void authenticate_success() {
        AppUser appUser = dao.authenticate(testA1);
        Assertions.assertEquals(testA1.getId(), appUser.getId());
    }

    @Test
    @Order(4)
    void authenticate_fail() {
        Exception e = Assertions.assertThrows(NotAuthenticatedException.class, () -> {
            testA1.setPw("WrongPassword");
            dao.authenticate(testA1);
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
        testA2 = new AppUser(
                0,
                "Rubeus",
                "Hagrid",
                "rhagrid@hogwarts.edu",
                2,
                "hagrid123");
        testA3 = new AppUser(
                0,
                "Luna",
                "Lovegood",
                "llovegood@hogwarts.edu",
                1,
                "lovegood123");

        dao.createAppUser(testA2);
        dao.createAppUser(testA3);

        Set<AppUser> allAppUsers = dao.getAllAppUsers();
        Assertions.assertTrue(allAppUsers.size() > 2);
    }

    @Test
    @Order(7)
    void delete_app_user_by_id() {
        Assertions.assertTrue(dao.deleteAppUserById(testA1.getId()));
        Assertions.assertTrue(dao.deleteAppUserById(testA2.getId()));
        Assertions.assertTrue(dao.deleteAppUserById(testA3.getId()));
    }
}
