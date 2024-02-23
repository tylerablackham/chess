package dataAccess;

import model.AuthData;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private HashMap<String, AuthData> authMap = new HashMap<>();
    @Override
    public String createAuth(AuthData authData) throws DataAccessException {
        authMap.put(authData.authToken(), authData);
        return authData.authToken();
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return authMap.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        authMap.remove(authToken);
    }

    @Override
    public void clear() {
        authMap = new HashMap<>();
    }
}
