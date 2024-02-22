package dataAccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO{
    @Override
    public void creatUser(UserData userData) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }
}
