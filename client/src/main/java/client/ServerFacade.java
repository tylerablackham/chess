package client;
import chess.ChessMove;
import com.google.gson.Gson;
import model.*;
import webSocketMessages.userCommands.JoinObserver;

import java.io.IOException;

public class ServerFacade {
    private String serverUrl;
    private WebSocketCommunicator webSocketCommunicator;

    public ServerFacade(int port) {
        serverUrl = "http://localhost:" + port;
    }

    public void addObserver(ServerMessageObserver serverMessageObserver) throws Exception {
        webSocketCommunicator = new WebSocketCommunicator(serverUrl, serverMessageObserver);
    }

    public AuthData register(UserData userData) throws IOException {
        String response = HTTPCommunicator.sendRequest(serverUrl, "/user", "POST",
                null, userData);
        return new Gson().fromJson(response, AuthData.class);
    }

    public AuthData login(LoginRequest loginRequest) throws IOException {
        String response = HTTPCommunicator.sendRequest(serverUrl, "/session", "POST",
                null, loginRequest);
        return new Gson().fromJson(response, AuthData.class);
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest) throws IOException {
        String response = HTTPCommunicator.sendRequest(serverUrl, "/game", "POST",
                createGameRequest.authToken(), createGameRequest);
        return new Gson().fromJson(response, CreateGameResponse.class);
    }

    public GameList listGames(AuthToken authToken) throws IOException {
        String response = HTTPCommunicator.sendRequest(serverUrl, "/game", "GET",
                authToken.authToken(), null);
        return new Gson().fromJson(response, GameList.class);
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws IOException {
        HTTPCommunicator.sendRequest(serverUrl, "/game", "PUT",
                joinGameRequest.authToken(), joinGameRequest);
    }

    public void joinPlayer(JoinGameRequest joinGameRequest) throws IOException {
        webSocketCommunicator.joinPlayer(joinGameRequest);
    }

    public void joinObserver(JoinGameRequest joinGameRequest) throws IOException{
        webSocketCommunicator.joinObserver(joinGameRequest);
    }

    public void logout(AuthToken authToken) throws IOException {
        HTTPCommunicator.sendRequest(serverUrl, "/session", "DELETE",
                authToken.authToken(), null);
    }

    public void makeMove(AuthToken authToken, int gameID, ChessMove move) throws IOException {
        webSocketCommunicator.makeMove(authToken, gameID, move);
    }

    public void resign(AuthToken authToken, int gameID) throws IOException {
        webSocketCommunicator.resign(authToken, gameID);
    }

    public void leave(AuthToken authToken, int gameID) throws IOException {
        webSocketCommunicator.leave(authToken, gameID);
    }
}
