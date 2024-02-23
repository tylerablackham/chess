package dataAccess;

import model.GameData;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private HashMap<Integer, GameData> gameMap = new HashMap<>();
    @Override
    public int createGame(GameData gameData) throws DataAccessException {
        try{
            gameMap.put(gameData.gameID(), gameData);
        }
        catch(Exception e) {
            throw new DataAccessException("Unable to create new game:\n\t" + e.getMessage());
        }
        return gameData.gameID();
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try{
            return gameMap.get(gameID);
        }
        catch(Exception e) {
            throw new DataAccessException("Unable to get game:\n\t" + e.getMessage());
        }
    }

    @Override
    public GameData[] listGames() throws DataAccessException {
        try{
            return gameMap.values().toArray(new GameData[0]);
        }
        catch(Exception e) {
            throw new DataAccessException("Unable to get list of games:\n\t" + e.getMessage());
        }
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        try{
            gameMap.replace(gameData.gameID(), gameData);
        }
        catch(Exception e) {
            throw new DataAccessException("Unable to update game:\n\t" + e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try{
            gameMap = new HashMap<>();
        }
        catch(Exception e) {
            throw new DataAccessException("Unable to clear games:\n\t" + e.getMessage());
        }
    }
}
