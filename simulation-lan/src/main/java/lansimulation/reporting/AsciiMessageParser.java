package lansimulation.reporting;

import java.util.Optional;

public class AsciiMessageParser implements IMessageParser{

    protected static final String ASCII_DOCUMENT_TITLE = "ASCII DOCUMENT";
    protected static final String DEFAULT_AUTHOR = "Unknown";

    @Override
    public MessageContent parseMessage(RawMessage rawMessage) {
        MessageContent messageContent = new MessageContent(ASCII_DOCUMENT_TITLE, DEFAULT_AUTHOR);

        getAuthorFromFixedPosition(rawMessage.getMessage())
                .ifPresent(messageContent::setAuthor);

        messageContent.setJobType("ASCII Print");
        return messageContent;
    }

    public Optional<String> getAuthorFromFixedPosition(String message) {
        if (message.length() >= 16) {
            return Optional.of(message.substring(8, 16));
        }

        return Optional.empty();
    }
}
