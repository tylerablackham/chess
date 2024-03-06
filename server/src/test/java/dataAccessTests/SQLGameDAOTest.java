package dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.SQLGameDAO;
import model.GameData;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SQLGameDAOTest {

    private SQLGameDAO gameDAO;
    @BeforeEach
    void setUp() throws DataAccessException {
        gameDAO = new SQLGameDAO();
        gameDAO.clear();
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        gameDAO.clear();
    }

    @Test
    void positiveCreateGame() {
        Assertions.assertDoesNotThrow(() -> {
            gameDAO.createGame(new GameData(1, null, null, "game1", new ChessGame()));
        });
    }

    @Test
    void negativeCreateGame() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.createGame(new GameData(0, null, null, null, new ChessGame()));
        });
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.createGame(new GameData(0, null, null, "game1", null));
        });
    }

    @Test
    void positiveGetGame() throws DataAccessException {
        gameDAO.createGame(new GameData(1, null, null, "game1", new ChessGame()));
        GameData game = gameDAO.getGame(1);
        Assertions.assertEquals("game1", game.gameName());
        Assertions.assertEquals(new ChessGame().getBoard(), game.game().getBoard());
    }

    @Test
    void negativeGetGame() throws DataAccessException {
        GameData game = gameDAO.getGame(1);
        Assertions.assertNull(game);
    }

    @Test
    void positiveListGames() throws DataAccessException {
        gameDAO.createGame(new GameData(1, null, null, "game1", new ChessGame()));
        gameDAO.createGame(new GameData(2, null, null, "game2", new ChessGame()));
        gameDAO.createGame(new GameData(3, null, null, "game3", new ChessGame()));
        ArrayList<GameData> games = gameDAO.listGames();
        Assertions.assertEquals(3, games.size());
        Assertions.assertEquals("game1", games.get(0).gameName());
        Assertions.assertEquals("game2", games.get(1).gameName());
        Assertions.assertEquals("game3", games.get(2).gameName());
    }

    @Test
    void negativeListGames() throws DataAccessException {
        ArrayList<GameData> games = gameDAO.listGames();
        Assertions.assertEquals(0, games.size());
    }

    @Test
    void positiveUpdateGame() throws DataAccessException {
        gameDAO.createGame(new GameData(1, null, null, "game1", new ChessGame()));
        gameDAO.updateGame(new GameData(1, "white", null, "game1", new ChessGame()));
        Assertions.assertEquals("white", gameDAO.getGame(1).whiteUsername());
        gameDAO.updateGame(new GameData(1, "white", "black", "game1", new ChessGame()));
        Assertions.assertEquals("black", gameDAO.getGame(1).blackUsername());
    }

    @Test
    void negativeUpdateGame() throws DataAccessException {
        gameDAO.createGame(new GameData(1, null, null, "game1", new ChessGame()));
        gameDAO.updateGame(new GameData(2, "white", null, "game1", new ChessGame()));
        Assertions.assertNull(gameDAO.getGame(2));
        Assertions.assertNull(gameDAO.getGame(1).whiteUsername());
    }

    @Test
    void clear() throws DataAccessException {
        gameDAO.createGame(new GameData(1, null, null, "game1", new ChessGame()));
        gameDAO.createGame(new GameData(2, null, null, "game2", new ChessGame()));
        gameDAO.createGame(new GameData(3, null, null, "game3", new ChessGame()));
        gameDAO.clear();
        Assertions.assertEquals(0, gameDAO.listGames().size());
        Assertions.assertNull(gameDAO.getGame(1));
        Assertions.assertNull(gameDAO.getGame(2));
        Assertions.assertNull(gameDAO.getGame(3));
    }
}