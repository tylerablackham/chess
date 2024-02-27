package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.AuthToken;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.DatabaseService;
import service.UserService;

public class UserServiceTests {
    private MemoryUserDAO memoryUserDAO;
    private MemoryAuthDAO memoryAuthDAO;
    private UserService userService;
    @BeforeEach
    void setUp() {
        memoryUserDAO = new MemoryUserDAO();
        memoryAuthDAO = new MemoryAuthDAO();
        userService = new UserService(memoryUserDAO, memoryAuthDAO);
    }

    @Test
    @DisplayName("Register - Positive Test")
    void positiveRegister() throws DataAccessException {
        UserData userData = new UserData("username", "password", "email@email.com");
        AuthData authData = userService.register(userData);
        Assertions.assertEquals("password",memoryUserDAO.getUser("username").password());
        Assertions.assertEquals("email@email.com",memoryUserDAO.getUser("username").email());
        Assertions.assertEquals("username",memoryAuthDAO.getAuth(authData.authToken()).username());
    }

    @Test
    @DisplayName("Register - Negative Test")
    void negativeRegister() {
        UserData userData1 = new UserData("username", "password", "email@email.com");
        UserData userData2 = new UserData("username", "password", "email@email.com");
        Assertions.assertThrows(DataAccessException.class, () -> {
            AuthData authData1 = userService.register(userData1);
            AuthData authData2 = userService.register(userData2);
        }, "User Already Exists");
    }

    @Test
    @DisplayName("Login - Positive Test")
    void positiveLogin() throws DataAccessException {
        UserData userData = new UserData("username", "password", "email@email.com");
        LoginRequest loginRequest = new LoginRequest("username", "password");
        userService.register(userData);
        AuthData authData = userService.login(loginRequest);
        Assertions.assertEquals("username", memoryAuthDAO.getAuth(authData.authToken()).username());
    }

    @Test
    @DisplayName("Login - Negative Test")
    void negativeLogin() {
        LoginRequest loginRequest = new LoginRequest("username", "password");
        Assertions.assertThrows(DataAccessException.class, () -> {
            AuthData authData = userService.login(loginRequest);
            Assertions.assertEquals("username", memoryAuthDAO.getAuth(authData.authToken()).username());
        }, "Unauthorized");
    }

    @Test
    @DisplayName("Logout - Positive Test")
    void positiveLogout() throws DataAccessException {
        UserData userData = new UserData("username", "password", "email@email.com");
            AuthData authData = userService.register(userData);
            userService.logout(new AuthToken(authData.authToken()));
            Assertions.assertNull(memoryAuthDAO.getAuth(authData.authToken()));
    }

    @Test
    @DisplayName("Logout - Negative Test")
    void negativeLogout() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.logout(new AuthToken("bad auth token"));
        }, "Unauthorized" );
    }
}
