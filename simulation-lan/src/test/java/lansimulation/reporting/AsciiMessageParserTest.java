package lansimulation.reporting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AsciiMessageParserTest {

    private AsciiMessageParser testee;

    @BeforeEach
    public void setUp() {
        testee = new AsciiMessageParser();
    }

    @Test
    public void test_parseMessage_titleIsFixed() {
        final String anyMessage = "AuthorA>AnyMessage";
        final RawMessage input = new RawMessage(anyMessage, MessageType.ASCII);

        // act
        MessageContent out = testee.parseMessage(input);

        // assert
        assertEquals(AsciiMessageParser.ASCII_DOCUMENT_TITLE, out.getTitle());
    }

    @Test
    public void test_parseMessage_authorIsTakenFromFixedPosition() {
        final String expectedAuthor = "Expected";
        final String messageWithValidAuthor = "AuthorA>" + expectedAuthor + "<test";
        final RawMessage input = new RawMessage(messageWithValidAuthor, MessageType.ASCII);

        // act
        MessageContent out = testee.parseMessage(input);

        // assert
        assertEquals(expectedAuthor, out.getAuthor());
    }

    @Test
    public void test_parseMessage_authorIsDefaultWhenMessageIsTooSmall() {
        final String tooShortMessage = "AuthorA>ToSmall";
        final RawMessage input = new RawMessage(tooShortMessage, MessageType.ASCII);

        // act
        MessageContent out = testee.parseMessage(input);

        // assert
        assertEquals(AsciiMessageParser.DEFAULT_AUTHOR, out.getAuthor());
    }
}