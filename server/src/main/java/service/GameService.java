package service;

import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.*;
import chess.ChessGame.TeamColor;

public class GameService {
    static int gameID;
    GameDAO gameDAO;
    AuthDAO authDAO;
    public GameService(GameDAO gameDAO, AuthDAO authDAO){
        gameID = 1;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    public GameList listGames(AuthToken authToken) throws DataAccessException {
        try {
            if (authDAO.getAuth(authToken.authToken()) != null) {
                return new GameList(gameDAO.listGames());
            }
            else {
                throw new DataAccessException("Unauthorized");
            }
        }
        catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    public CreateGameResponse createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        try {
            if (authDAO.getAuth(createGameRequest.authToken()) != null){
                GameData game = new GameData(gameID, null, null, createGameRequest.gameName(), new ChessGame());
                gameID++;
                gameDAO.createGame(game);
                return new CreateGameResponse(game.gameID());
            }
            else {
                throw new DataAccessException("Unauthorized");
            }
        }
        catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    public void joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        try {
            AuthData auth = authDAO.getAuth(joinGameRequest.authToken());
            if (auth != null) {
                GameData game = gameDAO.getGame(joinGameRequest.gameID());
                if (game!= null){
                    GameData newGame;
                    if (joinGameRequest.playerColor() == TeamColor.WHITE){
                        if (game.whiteUsername() != null) {
                            throw new DataAccessException("Taken");
                        }
                        newGame = new GameData(joinGameRequest.gameID(), auth.username(), game.blackUsername(), game.gameName(), game.game());
                    }
                    else if (joinGameRequest.playerColor() == TeamColor.BLACK) {
                        if (game.blackUsername() != null) {
                            throw new DataAccessException("Taken");
                        }
                        newGame = new GameData(joinGameRequest.gameID(), game.whiteUsername(), auth.username(), game.gameName(), game.game());
                    }
                    else {
                        newGame = new GameData(joinGameRequest.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
                    }
                    gameDAO.updateGame(newGame);
                }
                else {
                    throw new DataAccessException("Game Does Not Exist");
                }
            }
            else {
                throw new DataAccessException("Unauthorized");
            }
        }
        catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }

    }
}
