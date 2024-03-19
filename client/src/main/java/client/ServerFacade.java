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
        URL url = new URL(serverUrl + "/user");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);

        http.connect();

        try(OutputStream body = http.getOutputStream()) {
            byte[] input = new Gson().toJson(userData).getBytes();
            body.write(input, 0, input.length);
        }
        int responseCode = http.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return new Gson().fromJson(response.toString(), AuthData.class);
            }
        } else {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(http.getErrorStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                Error error = new Gson().fromJson(response.toString(), Error.class);
                throw new IOException("Server error: " + error.message());
            }
        }
    }
    public AuthData login(LoginRequest loginRequest) throws IOException {
        URL url = new URL(serverUrl + "/session");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);

        http.connect();

        try(OutputStream body = http.getOutputStream()) {
            byte[] input = new Gson().toJson(loginRequest).getBytes();
            body.write(input, 0, input.length);
        }
        int responseCode = http.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return new Gson().fromJson(response.toString(), AuthData.class);
            }
        } else {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(http.getErrorStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                Error error = new Gson().fromJson(response.toString(), Error.class);
                throw new IOException("Server error: " + error.message());
            }
        }
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest) throws IOException {
        URL url = new URL(serverUrl + "/game");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Authorization", createGameRequest.authToken());

        http.connect();

        try(OutputStream body = http.getOutputStream()) {
            byte[] input = new Gson().toJson(createGameRequest).getBytes();
            body.write(input, 0, input.length);
        }
        int responseCode = http.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return new Gson().fromJson(response.toString(), CreateGameResponse.class);
            }
        } else {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(http.getErrorStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                Error error = new Gson().fromJson(response.toString(), Error.class);
                throw new IOException("Server error: " + error.message());
            }
        }
    }

    public GameList listGames(AuthToken authToken) {
        return null;
    }

    public void joinGame(JoinGameRequest joinGameRequest) {

    }

    public void logout(AuthToken authToken) {

    }

}
