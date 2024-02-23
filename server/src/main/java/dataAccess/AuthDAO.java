package dataAccess;

import model.AuthData;

public interface AuthDAO {
    public String createAuth(AuthData authData) throws DataAccessException; //returns AuthToken after creating Auth
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(String authToken) throws DataAccessException;
    public void clear() throws DataAccessException;
}
