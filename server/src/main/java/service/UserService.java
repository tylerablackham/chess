package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.LoginRequest;
import model.AuthTokenRequest;
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
    public AuthData login(LoginRequest loginRequest) throws DataAccessException {
        try {
            if(userDAO.getUser(loginRequest.username()) != null){
                if(userDAO.getUser(loginRequest.username()).password() == loginRequest.password()){
                    AuthData auth = new AuthData(UUID.randomUUID().toString(), loginRequest.username());
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
    public void logout(AuthTokenRequest authTokenRequest) throws DataAccessException {
        try {
            if (authDAO.getAuth(authTokenRequest.authToken()) != null) {
                authDAO.deleteAuth(authTokenRequest.authToken());
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
