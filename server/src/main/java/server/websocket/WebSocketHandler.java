package server.websocket;


import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.*;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        Gson gson = new Gson();
        UserGameCommand action = gson.fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(session, gson.fromJson(message, JoinPlayer.class));
            case JOIN_OBSERVER -> joinObserver(session, gson.fromJson(message, JoinObserver.class));
            case MAKE_MOVE -> makeMove(session, gson.fromJson(message, MakeMove.class));
            case LEAVE -> leave(session, gson.fromJson(message, Leave.class));
            case RESIGN -> resign(session, gson.fromJson(message, Resign.class));
        }
    }

    private void joinPlayer (Session session, JoinPlayer joinPlayer) throws IOException {}

    private void joinObserver (Session session, JoinObserver joinObserver) throws IOException {}

    private void makeMove (Session session, MakeMove makeMove) throws IOException {}

    private void leave (Session session, Leave leave) throws IOException {}

    private void resign (Session session, Resign resign) throws IOException {}
}
