package dev.cheun.utiltests;

import com.auth0.jwt.interfaces.DecodedJWT;
import dev.cheun.daos.AppUserDAO;
import dev.cheun.entities.AppUser;
import dev.cheun.services.AppUserService;
import dev.cheun.services.AppUserServiceImpl;
import dev.cheun.utils.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class JwtTests {
    private static AppUser emp = null;
    private static AppUser mgr = null;

    @Mock
    private static AppUserDAO aDao = null;
    private static AppUserService aServ = null;

    @BeforeEach
    void setUp() {
        emp = new AppUser(
                1,
                "Ron",
                "Weasley",
                "rweasley@hogwarts.edu",
                1
        );
        mgr = new AppUser(
                2,
                "Rubeus",
                "Hagrid",
                "rhagrid@hotwarts.edu",
                2
        );
        aServ = new AppUserServiceImpl(aDao);
    }

    @Test
    void create_jwt_emp() {
        Mockito.when(aDao.getAppUserById(1)).thenReturn(emp);
        AppUser emp = aDao.getAppUserById(1);
        String jwt = JwtUtil.generate(emp.getId(), emp.getUserRole());
        Assertions.assertNotNull(jwt);
    }

    @Test
    void create_jwt_mgr() {
        Mockito.when(aDao.getAppUserById(2)).thenReturn(mgr);
        AppUser mgr = aDao.getAppUserById(2);
        String jwt = JwtUtil.generate(mgr.getId(), mgr.getUserRole());
        Assertions.assertNotNull(jwt);
    }

    @Test
    void decode_jwt() {
        Mockito.when(aDao.getAppUserById(1)).thenReturn(emp);
        AppUser emp = aDao.getAppUserById(1);
        String encJwt = JwtUtil.generate(emp.getId(), emp.getUserRole());
        DecodedJWT jwt = JwtUtil.isValidJWT(encJwt);
        int userId = jwt.getClaim("sub").asInt();
        int roleId = jwt.getClaim("role").asInt();
        Assertions.assertEquals(1, userId);
        Assertions.assertEquals(1, roleId);
    }
}
