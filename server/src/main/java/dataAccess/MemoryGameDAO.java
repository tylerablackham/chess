package dataAccess;

import model.GameData;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private HashMap<Integer, GameData> gameMap = new HashMap<>();
    @Override
    public int createGame(GameData gameData) throws DataAccessException {
        gameMap.put(gameData.gameID(), gameData);
        return gameData.gameID();
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return gameMap.get(gameID);
    }

    @Override
    public GameData[] listGames() throws DataAccessException {
        return gameMap.values().toArray(new GameData[0]);
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        gameMap.replace(gameData.gameID(), gameData);
    }

    @Override
    public void clear() throws DataAccessException {
        gameMap = new HashMap<>();
    }
}
