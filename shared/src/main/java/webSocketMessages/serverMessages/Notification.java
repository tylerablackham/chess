package webSocketMessages.serverMessages;

public class Notification extends ServerMessage {
    public final String message;

    public Notification(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
