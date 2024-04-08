package server.websocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String authString;
    public Session session;
    public int gameID;
    public ChessGame.TeamColor color;

    public Connection(String authString, int gameID, Session session, ChessGame.TeamColor color) {
        this.authString = authString;
        this.session = session;
        this.gameID = gameID;
        this.color = color;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}
