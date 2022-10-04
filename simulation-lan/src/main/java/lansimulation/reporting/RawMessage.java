package lansimulation.reporting;

public class RawMessage {

    private final String message;
    private final MessageType messageType;

    public RawMessage(String message, MessageType messageType) {
        this.message = message;
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public MessageType getMessageType() {
        return messageType;
    }
}
