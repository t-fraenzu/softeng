package lansimulation.reporting;

import java.util.Optional;

public class PsMessageParser implements IMessageParser{

    private static final String LABEL_VALUE_TERMINATION_SYMBOL = ".";

    protected static final String DEFAULT_AUTHOR = "Unknown";
    protected static final String DEFAULT_TITLE = "Untitled";

    private Optional<String> searchTitleInMessage(String message) {
        return searchValueOfLabel(message, "title");
    }

    private Optional<String> searchAuthorInMessage(String message) {
        return searchValueOfLabel(message, "author");
    }

    private Optional<String> searchValueOfLabel(String message, String labelName) {
        String label = labelName + ":";
        int startPos = message.indexOf(label);

        boolean containsLabel = startPos >= 0;

        if (containsLabel) {
            int indexOfValueStart = startPos + label.length();
            int indexOfValueEnd = calculateIndexForEndOfLabelValue(message, indexOfValueStart);

            return Optional.of(message.substring(indexOfValueStart, indexOfValueEnd));
        }

        return Optional.empty();
    }

    @Override
    public MessageContent parseMessage(RawMessage rawMessage) {
        final MessageContent messageContent = new MessageContent(DEFAULT_TITLE, DEFAULT_AUTHOR);

        Optional<String> authorInMessage = searchAuthorInMessage(rawMessage.getMessage());
        authorInMessage.ifPresent(messageContent::setAuthor);

        Optional<String> titleInMessage = searchTitleInMessage(rawMessage.getMessage());
        titleInMessage.ifPresent(messageContent::setTitle);

        return messageContent;
    }

    private static int calculateIndexForEndOfLabelValue(String message, int indexOfValueBeginn) {
        int endPos = message.indexOf(LABEL_VALUE_TERMINATION_SYMBOL, indexOfValueBeginn);
        if (endPos < 0) {
            endPos = message.length();
        }
        return endPos;
    }
}
