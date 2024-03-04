package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDAOTest {
    private SQLUserDAO userDAO;

    @BeforeEach
    void setup() throws DataAccessException {
        userDAO = new SQLUserDAO();
    }

    @AfterEach
    void cleanup() throws DataAccessException {
        userDAO.clear();
    }

    @Test
    void positiveCreateUser() {
        Assertions.assertDoesNotThrow(() -> {
            userDAO.createUser(new UserData("name", "pass", "email@email.com"));
        });
    }

    @Test
    void negativeCreateUser() {
        Assertions.assertThrows( DataAccessException.class, () -> {
            userDAO.createUser(new UserData(null, "pass", "email@email.com"));
        });
        Assertions.assertThrows( DataAccessException.class, () -> {
            userDAO.createUser(new UserData("name", null, "email@email.com"));
        });
        Assertions.assertThrows( DataAccessException.class, () -> {
            userDAO.createUser(new UserData("name", "pass", null));
        });
        Assertions.assertThrows( DataAccessException.class, () -> {
            userDAO.createUser(new UserData("name", "pass", "email@email.com"));
            userDAO.createUser(new UserData("name", "pass", "email@email.com"));
        });
    }

    @Test
    void positiveGetUser() throws DataAccessException {
            userDAO.createUser(new UserData("name", "pass", "email@email.com"));
            UserData user = userDAO.getUser("name");
            Assertions.assertEquals("pass", user.password());
    }

    @Test
    void negativeGetUser() throws DataAccessException {
            UserData user = userDAO.getUser("name");
            Assertions.assertNull(user);
    }

    @Test
    void clear() throws DataAccessException {
        userDAO.createUser(new UserData("name1", "pass", "email@email.com"));
        userDAO.createUser(new UserData("name2", "pass", "email@email.com"));
        userDAO.createUser(new UserData("name3", "pass", "email@email.com"));
        userDAO.clear();
        Assertions.assertNull(userDAO.getUser("name1"));
        Assertions.assertNull(userDAO.getUser("name2"));
        Assertions.assertNull(userDAO.getUser("name3"));
    }
}