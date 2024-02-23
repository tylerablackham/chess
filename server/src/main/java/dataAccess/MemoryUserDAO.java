package dataAccess;

import model.UserData;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    private HashMap<String, UserData> userMap = new HashMap<>();
    @Override
    public void creatUser(UserData userData) throws DataAccessException {
        userMap.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return userMap.get(username);
    }

    @Override
    public void clear() throws DataAccessException {
        userMap = new HashMap<>();
    }
}
