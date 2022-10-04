package lansimulation.reporting;

import java.util.EnumMap;
import java.util.Map;

public class MessageAdapter implements IMessageAdapter {

    public static final EnumMap<MessageType, IMessageParser> DEFAULT_REGISTRY = new EnumMap<>(Map.of(
            MessageType.PS, new PsMessageParser(),
            MessageType.ASCII, new AsciiMessageParser()));
    private final EnumMap<MessageType, IMessageParser> adapterStrategy;

    public MessageAdapter(EnumMap<MessageType, IMessageParser> adapterStrategy) {
        this.adapterStrategy = adapterStrategy;
    }

    public MessageContent adaptMessage(RawMessage rawMessage) {
        if (adapterStrategy.containsKey(rawMessage.getMessageType())) {
            return adapterStrategy.get(rawMessage.getMessageType()).parseMessage(rawMessage);
        }

        throw new UnsupportedOperationException("Adapt message for type: " + rawMessage.getMessageType() + " is not supported");
    }
}
