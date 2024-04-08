package server.websocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String authString, int gameID, Session session, ChessGame.TeamColor color) {
        var connection = new Connection(authString, gameID, session, color);
        connections.put(authString, connection);
    }

    public void remove(String authString) {
        connections.remove(authString);
    }

    public String getByGameAndColor(int gameID, ChessGame.TeamColor color) {
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.gameID == gameID){
                    if (c.color == color) {
                        return c.authString;
                    }
                }
            }
        }
        return null;
    }

    public void broadcast(String exclude, int gameID, ServerMessage notification, String only) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.gameID == gameID){
                    if (!c.authString.equals(exclude)) {
                        if (c.authString.equals(only) || only == null){
                            c.send(notification.toString());
                        }
                    }
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.authString);
        }
    }
}
