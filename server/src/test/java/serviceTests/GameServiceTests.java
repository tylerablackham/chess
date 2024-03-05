package serviceTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.UserService;

public class GameServiceTests {
    private MemoryGameDAO memoryGameDAO;
    private MemoryAuthDAO memoryAuthDAO;
    private GameService gameService;
    @BeforeEach
    void setUp() throws DataAccessException {
        memoryGameDAO = new MemoryGameDAO();
        memoryAuthDAO = new MemoryAuthDAO();
        gameService = new GameService(memoryGameDAO, memoryAuthDAO);
        memoryAuthDAO.createAuth(new AuthData("AuthToken", "username"));
    }

    @Test
    @DisplayName("List Games - Positive Test")
    void positiveListGames() throws DataAccessException {
        memoryGameDAO.createGame(new GameData(1, "","", "Game1", null));
        memoryGameDAO.createGame(new GameData(2, "","", "Game2", null));
        GameList gameList = gameService.listGames(new AuthToken("AuthToken"));
        Assertions.assertEquals(2, gameList.games().size());
        Assertions.assertEquals("Game2", gameList.games().get(1).gameName());
    }

    @Test
    @DisplayName("List Games - Negative Test")
    void negativeListGames() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.listGames(new AuthToken("Bad AuthToken"));
        }, "Unauthorized");
    }

    @Test
    @DisplayName("Create Game - Positive Test")
    void positiveCreateGame() throws DataAccessException {
        CreateGameResponse createGameResponse = gameService.createGame(new CreateGameRequest("AuthToken", "Game1"));
        Assertions.assertEquals(1, memoryGameDAO.listGames().size());
        Assertions.assertEquals("Game1", memoryGameDAO.getGame(createGameResponse.gameID()).gameName());
    }

    @Test
    @DisplayName("Create Game - Negative Test")
    void negativeCreateGame() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.createGame(new CreateGameRequest("Bad AuthToken", "Game1"));
        }, "Unauthorized");
    }

    @Test
    @DisplayName("Join Game - Positive Test")
    void positiveJoinGame() throws DataAccessException {
        memoryGameDAO.createGame(new GameData(1, null,null, "Game1", null));
        gameService.joinGame(new JoinGameRequest("AuthToken", 1, ChessGame.TeamColor.WHITE));
        Assertions.assertEquals("username", memoryGameDAO.getGame(1).whiteUsername());
        gameService.joinGame(new JoinGameRequest("AuthToken", 1, ChessGame.TeamColor.BLACK));
        Assertions.assertEquals("username", memoryGameDAO.getGame(1).blackUsername());
        gameService.joinGame(new JoinGameRequest("AuthToken", 1, null));
    }

    @Test
    @DisplayName("Join Game - Negative Test")
    void negativeJoinGame() throws DataAccessException {
        memoryGameDAO.createGame(new GameData(1, "white","black", "Game1", null));
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(new JoinGameRequest("Bad AuthToken", 1, null));
        }, "Unauthorized");
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(new JoinGameRequest("AuthToken", 3, null));
        }, "Game Does Not Exist");
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(new JoinGameRequest("AuthToken", 1, ChessGame.TeamColor.WHITE));
        }, "Taken");
    }

}
