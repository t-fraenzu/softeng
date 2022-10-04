package lansimulation.reporting;

import java.util.EnumMap;
import java.util.Map;

public class MessageAdapter implements IMessageAdapter {

    private final EnumMap<MessageType, IMessageParser> parserRegistry = new EnumMap<>(Map.of(
            MessageType.PS, new PsMessageParser(),
            MessageType.ASCII, new AsciiMessageParser()));

    public MessageContent adaptMessage(RawMessage rawMessage) {
        if (parserRegistry.containsKey(rawMessage.getMessageType())) {
            return parserRegistry.get(rawMessage.getMessageType()).parseMessage(rawMessage);
        }

        throw new UnsupportedOperationException("Adapt message for type: " + rawMessage.getMessageType() + " is not supported");
    }
}
