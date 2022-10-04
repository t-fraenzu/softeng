package lansimulation.reporting;

import java.util.Optional;

public class MessageParser {

    private static final String LABEL_VALUE_TERMINATION_SYMBOL = ".";

    public Optional<String> searchTitleInMessage(String message) {
        return searchValueOfLabel(message, "title");
    }

    public Optional<String> searchAuthorInMessage(String message) {
        return searchValueOfLabel(message, "author");
    }

    public Optional<String> getAuthorFromFixedPosition(String message) {
        if (message.length() >= 16) {
            return Optional.of(message.substring(8, 16));
        }

        return Optional.empty();
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

    private static int calculateIndexForEndOfLabelValue(String message, int indexOfValueBeginn) {
        int endPos = message.indexOf(LABEL_VALUE_TERMINATION_SYMBOL, indexOfValueBeginn);
        if (endPos < 0) {
            endPos = message.length();
        }
        return endPos;
    }

}
