package client;

import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;
import model.*;

import javax.websocket.ContainerProvider;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

public class ServerFacade {
    Session session;


    public ServerFacade(String url) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message){}
            });

        } catch (URISyntaxException e) {
            throw new Exception(e.getMessage());
        }
    }

    public String register(UserData userData){
        return "";
    }
    public String login(LoginRequest loginRequest){
        return"";
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest) {
        return null;
    }

    public GameList listGames(AuthToken authToken) {
        return null;
    }

    public void joinGame(JoinGameRequest joinGameRequest) {

    }

    public void logout(AuthToken authToken) {

    }

}
