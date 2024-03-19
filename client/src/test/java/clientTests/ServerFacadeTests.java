package clientTests;

import dataAccess.*;
import model.AuthData;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import client.ServerFacade;

import java.io.IOException;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    private static SQLUserDAO userDAO = new SQLUserDAO();
    private static SQLGameDAO gameDAO = new SQLGameDAO();
    private static SQLAuthDAO authDAO = new SQLAuthDAO();

    @BeforeAll
    public static void init() {
        server = new Server(userDAO, gameDAO, authDAO);
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() throws DataAccessException {
        server.stop();
        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();
    }

    @Test
    public void positiveRegister() throws IOException, DataAccessException {
        AuthData authData = facade.register(new UserData("user", "pass", "email"));
        Assertions.assertNotNull(authData);
        Assertions.assertNotNull(userDAO.getUser("user"));
        Assertions.assertEquals("user", authData.username());
    }
    @Test
    public void negativeRegister() {
        Assertions.assertThrows(IOException.class, () -> {
            facade.register(new UserData(null, "pass", "email"));
        });
        Assertions.assertThrows(IOException.class, () -> {
            facade.register(new UserData("user", null, "email"));
        });
        Assertions.assertThrows(IOException.class, () -> {
            facade.register(new UserData("user", "pass", null));
        });
    }
    @Test
    public void positiveLogin() throws IOException, DataAccessException {
        userDAO.createUser(new UserData("user", "pass", "email"));
        AuthData authData = facade.login(new LoginRequest("user", "pass"));
        Assertions.assertNotNull(authData);
        Assertions.assertEquals("user", authData.username());
    }
    @Test
    public void negativeLogin() {
        Assertions.assertThrows(IOException.class, () -> {
           facade.login(new LoginRequest("user", "pass"));
        });
    }
    @Test
    public void positiveCreateGame() {

    }
    @Test
    public void negativeCreateGame() {

    }
    @Test
    public void positiveListGames() {

    }
    @Test
    public void negativeListGames() {

    }
    @Test
    public void positiveJoinGame() {

    }
    @Test
    public void negativeJoinGame() {

    }
    @Test
    public void positiveLogout() {

    }
    @Test
    public void negativeLogout() {

    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
