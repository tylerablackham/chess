package clientTests;

import chess.ChessGame;
import dataAccess.*;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import client.ServerFacade;

import java.io.IOException;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    private static final SQLUserDAO userDAO = new SQLUserDAO();
    private static final SQLGameDAO gameDAO = new SQLGameDAO();
    private static final SQLAuthDAO authDAO = new SQLAuthDAO();

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
    public void positiveCreateGame() throws DataAccessException, IOException {
        userDAO.createUser(new UserData("user", "pass", "email"));
        authDAO.createAuth(new AuthData("auth", "user"));
        CreateGameResponse response = facade.createGame(new CreateGameRequest("auth", "game1"));
        Assertions.assertNotNull(response);
        Assertions.assertEquals( "game1", gameDAO.getGame(response.gameID()).gameName());
    }
    @Test
    public void negativeCreateGame() {
        Assertions.assertThrows(IOException.class, () -> {
            facade.createGame(new CreateGameRequest("auth", "game1"));
        });
    }
    @Test
    public void positiveListGames() throws DataAccessException, IOException {
        userDAO.createUser(new UserData("user", "pass", "email"));
        authDAO.createAuth(new AuthData("auth", "user"));
        gameDAO.createGame(new GameData(1, null, null, "game1", new ChessGame()));
        gameDAO.createGame(new GameData(2, null, null, "game2", new ChessGame()));
        GameList gameList = facade.listGames(new AuthToken("auth"));
        Assertions.assertNotNull(gameList);
        Assertions.assertEquals(2, gameList.games().size());
    }
    @Test
    public void negativeListGames() {
        Assertions.assertThrows(IOException.class, () -> {
            facade.listGames(new AuthToken("auth"));
        });
    }
    @Test
    public void positiveJoinGame() throws DataAccessException, IOException {
        userDAO.createUser(new UserData("user", "pass", "email"));
        authDAO.createAuth(new AuthData("auth", "user"));
        gameDAO.createGame(new GameData(1, null, null, "game1", new ChessGame()));
        facade.joinGame(new JoinGameRequest("auth", 1, null));
        Assertions.assertNull(gameDAO.getGame(1).whiteUsername());
        Assertions.assertNull(gameDAO.getGame(1).blackUsername());
        facade.joinGame(new JoinGameRequest("auth", 1, ChessGame.TeamColor.WHITE));
        Assertions.assertEquals("user", gameDAO.getGame(1).whiteUsername());
        facade.joinGame(new JoinGameRequest("auth", 1, ChessGame.TeamColor.BLACK));
        Assertions.assertEquals("user", gameDAO.getGame(1).blackUsername());
    }
    @Test
    public void negativeJoinGame() {
        Assertions.assertThrows(IOException.class, () -> {
            facade.joinGame(new JoinGameRequest("auth", 1, null));
        });
    }
    @Test
    public void positiveLogout() throws DataAccessException, IOException {
        userDAO.createUser(new UserData("user", "pass", "email"));
        authDAO.createAuth(new AuthData("auth", "user"));
        facade.logout(new AuthToken("auth"));
        Assertions.assertNull(authDAO.getAuth("auth"));
    }
    @Test
    public void negativeLogout() {
        Assertions.assertThrows(IOException.class, () -> {
            facade.logout(new AuthToken("auth"));
        });
    }
}
