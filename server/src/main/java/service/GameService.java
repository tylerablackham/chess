package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import model.GameData;
import chess.ChessGame.TeamColor;

public class GameService {
    GameDAO gameDAO;
    AuthDAO authDAO;
    public GameService(GameDAO gameDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    public GameData[] listGames(String authToken){
        return null;
    }
    public int createGame(String authToke, String gameName){
        return 0;
    }
    public void joinGame(String authToken, int gameID, TeamColor clientColor){

    }
}
