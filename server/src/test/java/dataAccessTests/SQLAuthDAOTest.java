package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthDAOTest {

    private SQLAuthDAO authDAO;

    @BeforeEach
    void setUp() throws DataAccessException {
        authDAO = new SQLAuthDAO();
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        authDAO.clear();
    }

    @Test
    void positiveCreateAuth() {
        Assertions.assertDoesNotThrow(() -> {
            authDAO.createAuth(new AuthData("token1", "name"));
            authDAO.createAuth(new AuthData("token2", "name"));
        });
    }

    @Test
    void negativeCreateAuth() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.createAuth(new AuthData(null, "name"));
        });
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.createAuth(new AuthData("token1", null));
        });
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.createAuth(new AuthData("token1", "user"));
            authDAO.createAuth(new AuthData("token1", "user"));
        });

    }

    @Test
    void positiveGetAuth() throws DataAccessException {
        authDAO.createAuth(new AuthData("token1", "name"));
        AuthData auth = authDAO.getAuth("token1");
        Assertions.assertEquals("name", auth.username());
    }

    @Test
    void negativeGetAuth() throws DataAccessException {
        AuthData auth = authDAO.getAuth("token1");
        Assertions.assertNull(auth);
    }

    @Test
    void positiveDeleteAuth() throws DataAccessException {
        authDAO.createAuth(new AuthData("token1", "name"));
        authDAO.deleteAuth("token1");
        Assertions.assertNull(authDAO.getAuth("token1"));
    }

    @Test
    void negativeDeleteAuth() throws DataAccessException {
        authDAO.createAuth(new AuthData("token1", "name"));
        authDAO.deleteAuth("token2");
        Assertions.assertNotNull(authDAO.getAuth("token1"));
    }

    @Test
    void clear() throws DataAccessException {
        authDAO.createAuth(new AuthData("token1", "name"));
        authDAO.createAuth(new AuthData("token2", "name"));
        authDAO.createAuth(new AuthData("token3", "name"));
        authDAO.clear();
        Assertions.assertNull(authDAO.getAuth("token1"));
        Assertions.assertNull(authDAO.getAuth("token2"));
        Assertions.assertNull(authDAO.getAuth("token3"));

    }
}