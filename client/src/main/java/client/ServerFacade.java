package client;
import com.google.gson.Gson;
import model.*;
import model.Error;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ServerFacade {
    private String serverUrl;

    public ServerFacade(int port) {
        serverUrl = "http://localhost:" + port;
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

    public void logout(AuthToken authToken) throws IOException {
        HTTPCommunicator.sendRequest(serverUrl, "/session", "DELETE",
                authToken.authToken(), null);
    }
}
