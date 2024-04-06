package client;

import chess.ChessMove;
import com.google.gson.Gson;
import model.AuthToken;
import model.JoinGameRequest;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketCommunicator {

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
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    serverMessageObserver.notify(serverMessage);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new Exception(e.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
//    @Override
//    public void onOpen(Session session, EndpointConfig endpointConfig) {
//    }

    public void joinPlayer (JoinGameRequest joinGameRequest) {

    }

    public void joinObserver (JoinGameRequest joinGameRequest) {

    }

    public void makeMove (AuthToken authToken, int gameId, ChessMove move) {

    }

    public void leave (AuthToken authToken, int gameId) {

    }

    public void resign(AuthToken authToken, int gameId) {

    }
}
