package service;

import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.AuthData;
import model.GameData;
import chess.ChessGame.TeamColor;

public class GameService {
    static int gameID;
    GameDAO gameDAO;
    AuthDAO authDAO;
    public GameService(GameDAO gameDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    public GameData[] listGames(String authToken) throws DataAccessException {
        try {
            if (authDAO.getAuth(authToken) != null) {
                return gameDAO.listGames();
            }
            else {
                throw new DataAccessException("Not Authorized");
            }
        }
        catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    public int createGame(String authToken, String gameName) throws DataAccessException {
        try {
            if (authDAO.getAuth(authToken) != null){
                GameData game = new GameData(gameID, null, null, gameName, new ChessGame());
                gameID++;
                gameDAO.createGame(game);
                return game.gameID();
            }
            else {
                throw new DataAccessException("Not Authorized");
            }
        }
        catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    public void joinGame(String authToken, int gameID, TeamColor clientColor) throws DataAccessException {
        try {
            AuthData auth = authDAO.getAuth(authToken);
            if (auth != null) {
                GameData game = gameDAO.getGame(gameID);
                if (game!= null){
                    GameData newGame;
                    if (clientColor == TeamColor.WHITE){
                        newGame = new GameData(gameID, auth.username(), game.blackUsername(), game.gameName(), game.game());
                    }
                    else {
                        newGame = new GameData(gameID, game.whiteUsername(), auth.username(), game.gameName(), game.game());
                    }
                    gameDAO.updateGame(newGame);
                }
                else {
                    throw new DataAccessException("DNE");
                }
            }
            else {
                throw new DataAccessException("Not Authorized");
            }
        }
        catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }

    }
}
