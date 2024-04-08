package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand{
    public final int gameID;

    public JoinObserver(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.JOIN_OBSERVER;
    }

    public int getGameID() {
        return gameID;
    }
}
