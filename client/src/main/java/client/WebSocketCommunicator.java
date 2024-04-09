package client;

import chess.ChessMove;
import com.google.gson.Gson;
import model.AuthToken;
import model.JoinGameRequest;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketCommunicator extends Endpoint {

    Session session;
    ServerMessageObserver serverMessageObserver;


    public WebSocketCommunicator(String url, ServerMessageObserver serverMessageObserver) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.serverMessageObserver = serverMessageObserver;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    try {
                        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                        serverMessageObserver.notify(serverMessage, message);
                    } catch (Exception e) {
                        serverMessageObserver.notify(new Error(e.getMessage()), message);
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new Exception(e.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinPlayer (JoinGameRequest joinGameRequest) throws IOException {
        JoinPlayer joinPlayer = new JoinPlayer(joinGameRequest.authToken(), joinGameRequest.gameID(), joinGameRequest.playerColor());
        this.session.getBasicRemote().sendText(new Gson().toJson(joinPlayer));
    }

    public void joinObserver (JoinGameRequest joinGameRequest) throws IOException {
        JoinObserver joinObserver = new JoinObserver(joinGameRequest.authToken(), joinGameRequest.gameID());
        this.session.getBasicRemote().sendText(new Gson().toJson(joinObserver));
    }

    public void makeMove (AuthToken authToken, int gameId, ChessMove move) throws IOException {
        MakeMove makeMove = new MakeMove(authToken.authToken(), gameId, move);
        this.session.getBasicRemote().sendText(new Gson().toJson(makeMove));
    }

    public void leave (AuthToken authToken, int gameId) throws IOException {
        Leave leave = new Leave(authToken.authToken(), gameId);
        this.session.getBasicRemote().sendText(new Gson().toJson(leave));
        this.session.close();
    }

    public void resign(AuthToken authToken, int gameId) throws IOException {
        Resign resign = new Resign(authToken.authToken(), gameId);
        this.session.getBasicRemote().sendText(new Gson().toJson(resign));
    }
}
