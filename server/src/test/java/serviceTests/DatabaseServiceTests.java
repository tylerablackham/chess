package serviceTests;

import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.DatabaseService;

class DatabaseServiceTests {
    private MemoryUserDAO memoryUserDAO;
    private MemoryGameDAO memoryGameDAO;
    private MemoryAuthDAO memoryAuthDAO;
    private DatabaseService databaseService;

    @BeforeEach
    void setup() {
        memoryAuthDAO = new MemoryAuthDAO();
        memoryGameDAO = new MemoryGameDAO();
        memoryUserDAO = new MemoryUserDAO();
        databaseService = new DatabaseService(memoryUserDAO, memoryGameDAO, memoryAuthDAO);
    }

    @Test
    @DisplayName("Clear Application - Positive Test")
    void clearApplication() throws DataAccessException {
        memoryUserDAO.createUser(new UserData("Username", "Password", "email"));
        memoryAuthDAO.createAuth(new AuthData("AuthToken", "Username"));
        memoryGameDAO.createGame(new GameData(1, "", "","Game1", null));
        memoryGameDAO.createGame(new GameData(2, "", "","Game2", null));
        databaseService.clearApplication();
        Assertions.assertNull(memoryUserDAO.getUser("Username"));
        Assertions.assertNull(memoryAuthDAO.getAuth("AuthToken"));
        Assertions.assertNull(memoryGameDAO.getGame(1));
        Assertions.assertEquals(0, memoryGameDAO.listGames().size());
    }
}