package dataAccess;

import model.UserData;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    private HashMap<String, UserData> userMap = new HashMap<>();
    @Override
    public void creatUser(UserData userData) throws DataAccessException {
        try{
            userMap.put(userData.username(), userData);
        }
        catch(Exception e) {
            throw new DataAccessException("Unable to create new user:\n\t" + e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try{
            return userMap.get(username);
        }
        catch(Exception e) {
            throw new DataAccessException("Unable to get user:\n\t" + e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try{
            userMap = new HashMap<>();
        }
        catch(Exception e) {
            throw new DataAccessException("Unable to clear users:\n\t" + e.getMessage());
        }
    }
}
