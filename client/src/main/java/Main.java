import chess.*;
import client.Menus;
import com.google.gson.*;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.lang.reflect.Type;

public class Main {
    public static void main(String[] args) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ServerMessage.class, new ServerMessageDeserializer());
        builder.registerTypeAdapter(UserGameCommand.class, new UserCommandDeserializer());
        Menus.main(args);
    }
    private static class ServerMessageDeserializer implements JsonDeserializer<ServerMessage> {
        @Override
        public ServerMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            String typeString = jsonObject.get("serverMessageType").getAsString();
            ServerMessage.ServerMessageType serverMessageType = ServerMessage.ServerMessageType.valueOf(typeString);

            return switch (serverMessageType) {
                case LOAD_GAME -> context.deserialize(jsonElement, LoadGame.class);
                case ERROR -> context.deserialize(jsonElement, Error.class);
                case NOTIFICATION -> context.deserialize(jsonElement, Notification.class);
            };
        }
    }

    private static class UserCommandDeserializer implements JsonDeserializer<UserGameCommand> {
        @Override
        public UserGameCommand deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            String typeString = jsonObject.get("commandType").getAsString();
            UserGameCommand.CommandType commandType = UserGameCommand.CommandType.valueOf(typeString);

            return switch (commandType) {
                case JOIN_PLAYER -> context.deserialize(jsonElement, JoinPlayer.class);
                case JOIN_OBSERVER -> context.deserialize(jsonElement, JoinObserver.class);
                case MAKE_MOVE -> context.deserialize(jsonElement, MakeMove.class);
                case LEAVE -> context.deserialize(jsonElement, Leave.class);
                case RESIGN -> context.deserialize(jsonElement, Resign.class);
            };
        }
    }
}