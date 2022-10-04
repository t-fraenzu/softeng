package lansimulation.reporting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PsMessageParserTest {

    private PsMessageParser testee;

    @BeforeEach
    public void setUp() {
        testee = new PsMessageParser();
    }

    @Test
    public void test_parseMessage_titleIsTakenFromField() {
        final String expectedTitle = "ExpectedTitleValue";
        final String messageWithValidTitle = "!PSSomeOtherTexttitle:" + expectedTitle;
        final RawMessage input = new RawMessage(messageWithValidTitle, MessageType.PS);

        // act
        MessageContent out = testee.parseMessage(input);

        // assert
        assertEquals(expectedTitle, out.getTitle());
    }

    @Test
    public void test_parseMessage_AuthorIsTakenFromMessage() {
        String expectedAuthor = "ExpectedAuthor";
        String messageWithValidAuthor = "!PSSomeOtherTextauthor:" + expectedAuthor;
        final RawMessage input = new RawMessage(messageWithValidAuthor, MessageType.PS);

        // act
        MessageContent out = testee.parseMessage(input);

        // assert
        assertEquals(expectedAuthor, out.getAuthor());
    }

    @Test
    public void test_parseMessage_authorIsDefaultWhenMessageContainsNoAuthor() {
        final String messageWithoutAuthor = "AuthorIsNotPresent";
        final RawMessage input = new RawMessage(messageWithoutAuthor, MessageType.PS);

        // act
        MessageContent out = testee.parseMessage(input);

        // assert
        assertEquals(PsMessageParser.DEFAULT_AUTHOR, out.getAuthor());
    }
}