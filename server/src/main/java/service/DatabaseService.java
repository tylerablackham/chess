package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class DatabaseService {
    UserDAO userDAO;
    GameDAO gameDAO;
    AuthDAO authDAO;
    public DatabaseService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    public void clearApplication() {

    }
}
