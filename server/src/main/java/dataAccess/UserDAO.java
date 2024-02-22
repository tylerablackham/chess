package dataAccess;

import model.UserData;

public interface UserDAO {
    public void creatUser(UserData userData) throws DataAccessException;
    public UserData getUser(String username) throws DataAccessException;
    public void clear() throws DataAccessException;
}
