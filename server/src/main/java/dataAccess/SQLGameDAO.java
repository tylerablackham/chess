package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDAO{

    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }
    @Override
    public int createGame(GameData gameData) throws DataAccessException {
        var statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        if (gameData.game() == null) {
            throw new DataAccessException("game must not be null");
        }
        var json = new Gson().toJson(gameData.game());
        SQLHelper.executeUpdate(statement, gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), json);
        return gameData.gameID();
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    while(rs.next()){
                        return new GameData(rs.getInt(1), rs.getString(2), rs.getString(3),
                                rs.getString(4), new Gson().fromJson(rs.getString(5), ChessGame.class));
                    }
                    return null;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException( String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    ArrayList<GameData> gameDataList = new ArrayList<>();
                    while(rs.next()){
                        gameDataList.add(new GameData(rs.getInt(1), rs.getString(2), rs.getString(3),
                                rs.getString(4), new Gson().fromJson(rs.getString(5), ChessGame.class)));
                    }
                    return gameDataList;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException( String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        var statement = "UPDATE game SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
        var json = new Gson().toJson(gameData.game());
        SQLHelper.executeUpdate(statement, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), json, gameData.gameID());
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE game";
        SQLHelper.executeUpdate(statement);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS game (
              `gameID` int NOT NULL,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `game` TEXT NOT NULL,
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
//        try (var conn = DatabaseManager.getConnection()) {
//            var statement = "SELECT COUNT(*) AS count FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = ?";
//            try (var ps = conn.prepareStatement(statement)) {
//                ps.setString(1, "chess");
//                try (var rs = ps.executeQuery()) {
//                    if(rs.getInt(1) == 0){
//                        DatabaseManager.createDatabase();
//                    }
//                }
//            }
//        } catch (Exception e) {
//            throw new DataAccessException( String.format("Unable to read data: %s", e.getMessage()));
//        }
        SQLHelper.configureDatabase(createStatements);
    }
}
