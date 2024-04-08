package server.websocket;


import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final SQLAuthDAO authDAO = new SQLAuthDAO();
    private final SQLGameDAO gameDAO = new SQLGameDAO();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        Gson gson = new Gson();
        UserGameCommand action = gson.fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(session, gson.fromJson(message, JoinPlayer.class));
            case JOIN_OBSERVER -> joinObserver(session, gson.fromJson(message, JoinObserver.class));
            case MAKE_MOVE -> makeMove(session, gson.fromJson(message, MakeMove.class));
            case LEAVE -> leave(gson.fromJson(message, Leave.class));
            case RESIGN -> resign(session, gson.fromJson(message, Resign.class));
        }
    }

    private void joinPlayer (Session session, JoinPlayer joinPlayer) throws IOException, DataAccessException {
        AuthData auth = authDAO.getAuth(joinPlayer.getAuthString());
        if (auth == null) {
            sendError(session, new Error("Error: bad auth token"));
            return;
        }
        GameData gameData = gameDAO.getGame(joinPlayer.getGameID());
        if (gameData == null) {
            sendError(session, new Error("Error: bad game ID"));
            return;
        }
        String name = auth.username();
        ChessGame game = gameData.game();
        String existing = joinPlayer.getPlayerColor() == ChessGame.TeamColor.WHITE ? gameData.whiteUsername() : gameData.blackUsername();
        if (!Objects.equals(existing, name)) {
            sendError(session, new Error("Error: This color player is already in use"));
            return;
        }
        String color = joinPlayer.getPlayerColor() == ChessGame.TeamColor.WHITE ? "white" : "black";
        connections.add(joinPlayer.getAuthString(), joinPlayer.getGameID(), session, joinPlayer.getPlayerColor());
        Notification notification = new Notification(name + " has joined the game as " + color + ".\n" );
        connections.broadcast(joinPlayer.getAuthString(), joinPlayer.getGameID(), notification, null);
        connections.broadcast(null, joinPlayer.getGameID(), new LoadGame(game), joinPlayer.getAuthString());
    }

    private void joinObserver (Session session, JoinObserver joinObserver) throws IOException, DataAccessException {
        AuthData auth = authDAO.getAuth(joinObserver.getAuthString());
        if (auth == null) {
            sendError(session, new Error("Error: bad authToken"));
            return;
        }
        GameData gameData = gameDAO.getGame(joinObserver.getGameID());
        if (gameData == null) {
            sendError(session, new Error("Error: bad gameID"));
            return;
        }
        String name = auth.username();
        ChessGame game = gameData.game();
        connections.add(joinObserver.getAuthString(), joinObserver.getGameID(), session, null);
        Notification notification = new Notification(name + " has joined as an observer.\n" );
        connections.broadcast(joinObserver.getAuthString(), joinObserver.getGameID(), notification, null);
        connections.broadcast(null, joinObserver.getGameID(), new LoadGame(game), joinObserver.getAuthString());
    }

    private void makeMove (Session session, MakeMove makeMove) throws IOException, DataAccessException {
        GameData gameData = gameDAO.getGame(makeMove.getGameID());
        ChessGame game = gameData.game();
        ChessMove move = makeMove.getMove();
        String name = authDAO.getAuth(makeMove.getAuthString()).username();
        String currentTurn = game.getTeamTurn() == ChessGame.TeamColor.WHITE ?
                connections.getByGameAndColor(makeMove.getGameID(), ChessGame.TeamColor.WHITE) :
                connections.getByGameAndColor(makeMove.getGameID(), ChessGame.TeamColor.BLACK);
        try {
            if (!Objects.equals(makeMove.getAuthString(), currentTurn)) {
                throw new InvalidMoveException("It is not your turn.");
            }
            game.makeMove(move);
            gameDAO.updateGame(new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                    gameData.gameName(), game));
            Notification notification = new Notification(name + " has moved " + ChessGame.getMoveString(move.getStartPosition()) +
                    " to " + ChessGame.getMoveString(move.getEndPosition()) + ".\n");
            connections.broadcast(null, makeMove.getGameID(), new LoadGame(game), null);
            connections.broadcast(makeMove.getAuthString(), makeMove.getGameID(), notification, null);
        } catch (InvalidMoveException e) {
            sendError(session, new Error("Error: " + e.getMessage()));
        }
    }

    private void leave (Leave leave) throws IOException, DataAccessException {
        connections.remove(leave.getAuthString());
        String name = authDAO.getAuth(leave.getAuthString()).username();
        GameData gameData = gameDAO.getGame(leave.getGameID());
        if (Objects.equals(gameData.whiteUsername(), name)){
            gameDAO.updateGame(new GameData(gameData.gameID(), null, gameData.blackUsername(),
                    gameData.gameName(), gameData.game()));
        }
        if (Objects.equals(gameData.blackUsername(), name)){
            gameDAO.updateGame(new GameData(gameData.gameID(), gameData.whiteUsername(), null,
                    gameData.gameName(), gameData.game()));
        }
        Notification notification = new Notification(name + " has left the game.\n" );
        connections.broadcast(leave.getAuthString(), leave.getGameID(), notification, null);
    }

    private void resign (Session session, Resign resign) throws IOException, DataAccessException {
        String name = authDAO.getAuth(resign.getAuthString()).username();
        GameData gameData = gameDAO.getGame(resign.getGameID());
        ChessGame game = gameData.game();
        if (!Objects.equals(resign.getAuthString(), connections.getByGameAndColor(resign.getGameID(), ChessGame.TeamColor.WHITE)) &&
                !Objects.equals(resign.getAuthString(), connections.getByGameAndColor(resign.getGameID(), ChessGame.TeamColor.BLACK))) {
            sendError(session, new Error("Error: Observers cannot resign."));
            return;
        }
        if (game.over) {
            sendError(session, new Error("Error: game is already over."));
            return;
        }
        Notification notification = new Notification(name + " has resigned.\n" );
        connections.broadcast(null, resign.getGameID(), notification, null);
        game.over = true;
        gameDAO.updateGame(new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                gameData.gameName(), game));
    }

    private void sendError(Session session, Error error) throws IOException, DataAccessException {
        session.getRemote().sendString(error.toString());
    }
}
