package dataAccess;

import chess.ChessGame;
import model.GameData;

public interface GameDAO {
    public int createGame(GameData gameData) throws DataAccessException; //returns gameID after creating Game
    public GameData getGame(int gameID) throws DataAccessException;
    public GameData[] listGames() throws DataAccessException;
    public void updateGame(GameData gameData) throws DataAccessException;
    public void clear() throws DataAccessException;
}
