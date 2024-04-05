package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGame extends ServerMessage {
    private final ChessGame game;

    public LoadGame(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }
}
