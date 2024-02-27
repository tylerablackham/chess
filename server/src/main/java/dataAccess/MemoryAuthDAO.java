package dataAccess;

import model.AuthData;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private HashMap<String, AuthData> authMap = new HashMap<>();
    @Override
    public String createAuth(AuthData authData) throws DataAccessException {
        try{
            authMap.put(authData.authToken(), authData);
        }
        catch(Exception e) {
            throw new DataAccessException("Error: Unable to create new auth:\n\t" + e.getMessage());
        }
        return authData.authToken();
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try{
            return authMap.get(authToken);
        }
        catch(Exception e) {
            throw new DataAccessException("Error: Unable to get auth:\n\t" + e.getMessage());
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try{
            authMap.remove(authToken);
        }
        catch(Exception e) {
            throw new DataAccessException("Error: Unable to delete auth:\n\t" + e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException{
        try{
            authMap = new HashMap<>();
        }
        catch(Exception e) {
            throw new DataAccessException("Error: Unable to clear auths:\n\t" + e.getMessage());
        }
    }
}
