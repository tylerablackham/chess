package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.*;
import model.*;
import model.Error;
import server.websocket.WebSocketHandler;
import service.DatabaseService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.Objects;

public class Server {
    private final DatabaseService databaseService;
    private final GameService gameService;
    private final UserService userService;
    private final WebSocketHandler webSocketHandler;

    public static void main(String[] args) {
        new Server().run(8080);
    }

    public Server(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        databaseService = new DatabaseService(userDAO, gameDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
        userService = new UserService(userDAO, authDAO);
        webSocketHandler = new WebSocketHandler();
    }

    public Server() {
        this(new SQLUserDAO(), new SQLGameDAO(), new SQLAuthDAO());
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/connect", webSocketHandler);

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game",this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clear(Request req, Response res) {
        try {
            databaseService.clearApplication();
            res.status(200);
            return new JsonObject();
        }
        catch (DataAccessException e) {
            res.status(500);
            return new Gson().toJson(new Error(e.getMessage()));
        }
    }
    private Object register(Request req, Response res) {
        UserData userData = new Gson().fromJson(req.body(), UserData.class);
        if (userData.password() == null || userData.username() == null || userData.email() == null){
            res.status(400);
            return new Gson().toJson(new Error("Error: bad request"));
        }
        try {
            AuthData authData = userService.register(userData);
            res.status(200);
            return new Gson().toJson(authData);
        }
        catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "User Already Exists")){
                res.status(403);
                return new Gson().toJson(new Error("Error: already taken"));
            }
            res.status(500);
            return new Gson().toJson(new Error(e.getMessage()));
        }
    }
    private Object login(Request req, Response res) {
        LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        try {
            AuthData authData = userService.login(loginRequest);
            res.status(200);
            return new Gson().toJson(authData);
        }
        catch (DataAccessException e) {
            return checkUnauthorized(e, res);
        }
    }
    private Object logout(Request req, Response res) {
        AuthToken authToken = new AuthToken(req.headers("authorization"));
        try {
            userService.logout(authToken);
            res.status(200);
            return new JsonObject();
        }
        catch (DataAccessException e) {
            return checkUnauthorized(e, res);
        }
    }
    private Object listGames(Request req, Response res) {
        AuthToken authToken = new AuthToken(req.headers("authorization"));
        try {
            GameList gameList = gameService.listGames(authToken);
            res.status(200);
            return new Gson().toJson(gameList);
        }
        catch (DataAccessException e) {
            return checkUnauthorized(e, res);
        }
    }
    private Object createGame(Request req, Response res) {
        CreateGameRequest createGameRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);
        createGameRequest = new CreateGameRequest(req.headers("authorization"), createGameRequest.gameName());
        if (createGameRequest.gameName() == null || createGameRequest.authToken() == null) {
            res.status(400);
            return new Gson().toJson(new Error("Error: bad request"));
        }
        try {
            CreateGameResponse createGameResponse = gameService.createGame(createGameRequest);
            res.status(200);
            return new Gson().toJson(createGameResponse);
        }
        catch (DataAccessException e) {
            return checkUnauthorized(e, res);
        }
    }
    private Object joinGame(Request req, Response res) {
        JoinGameRequest joinGameRequest = new Gson().fromJson(req.body(), JoinGameRequest.class);
        joinGameRequest = new JoinGameRequest(req.headers("authorization"), joinGameRequest.gameID(), joinGameRequest.playerColor());
        if (joinGameRequest.authToken() == null) {
            res.status(400);
            return new Gson().toJson(new Error("Error: bad request"));
        }
        try {
            gameService.joinGame(joinGameRequest);
            res.status(200);
            return new JsonObject();
        }
        catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "Game Does Not Exist")){
                res.status(400);
                return new Gson().toJson(new Error("Error: bad request"));
            }
            if (Objects.equals(e.getMessage(), "Taken")){
                res.status(403);
                return new Gson().toJson(new Error("Error: already taken"));
            }
            return checkUnauthorized(e, res);
        }
    }

    private Object checkUnauthorized(DataAccessException e, Response res){
        if (Objects.equals(e.getMessage(), "Unauthorized")){
            res.status(401);
            return new Gson().toJson(new Error("Error: unauthorized"));
        }
        res.status(500);
        return new Gson().toJson(new Error(e.getMessage()));
    }
}
