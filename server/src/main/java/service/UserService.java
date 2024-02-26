package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    UserDAO userDAO;
    AuthDAO authDAO;
    public UserService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public AuthData register(UserData user) throws DataAccessException {
        try{
            if(userDAO.getUser(user.username()) == null){
                userDAO.creatUser(user);
                AuthData auth = new AuthData(UUID.randomUUID().toString(), user.username());
                authDAO.createAuth(auth);
                return auth;
            }
            else {
                throw new DataAccessException("User Already Exists");
            }
        }
        catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    public AuthData login(String username, String password) throws DataAccessException {
        try {
            if(userDAO.getUser(username) != null){
                if(userDAO.getUser(username).password() == password){
                    AuthData auth = new AuthData(UUID.randomUUID().toString(), username);
                    authDAO.createAuth(auth);
                    return auth;
                }
                else {
                    throw new DataAccessException("Wrong Password");
                }
            }
            else {
                throw new DataAccessException("DNE");
            }
        }
        catch (DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
    }
    public void logout(String authToken) throws DataAccessException {
        try {
            if (authDAO.getAuth(authToken) != null) {
                authDAO.deleteAuth(authToken);
            }
            else {
                throw new DataAccessException("DNE");
            }
        }
        catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
