package dataAccess;

import chess.ChessGame;
import model.GameData;

public interface GameDAO {
    public int createGame(String gameName); //returns gameID after creating Game
    public GameData getGame(int gameID);
    public GameData[] listGames();
    public void updateGame(int gameID, String username, String clientColor, ChessGame game);
    public void clear();
}
