package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {
    public final int gameID;

    public Resign(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.RESIGN;
    }

    public int getGameID() {
        return gameID;
    }
}
