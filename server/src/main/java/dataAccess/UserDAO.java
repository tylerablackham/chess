package dataAccess;

import model.UserData;

public interface UserDAO {
    public void creatUser(String username, String password, String email);
    public UserData getUser(String username);
    public void clear();
}
