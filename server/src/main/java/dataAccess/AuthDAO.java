package dataAccess;

import model.AuthData;

public interface AuthDAO {
    public String createAuth(String username); //returns AuthToken after creating Auth
    public AuthData getAuth(String authToken);
    public void deleteAuth(String authToken);
    public void clear();
}
