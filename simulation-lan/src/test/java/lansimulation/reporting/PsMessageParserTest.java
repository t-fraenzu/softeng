package lansimulation.reporting;

import lansimulation.internals.Node;
import lansimulation.internals.Packet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        final RawMessage input = new RawMessage(messageWithValidTitle);

        // act
        MessageContent out = testee.parseMessage(input);

        // assert
        assertEquals(expectedTitle, out.getTitle());
    }

    @Test
    public void test_parseMessage_AuthorIsTakenFromMessage() {
        String expectedAuthor = "ExpectedAuthor";
        String messageWithValidAuthor = "!PSSomeOtherTextauthor:" + expectedAuthor;
        final RawMessage input = new RawMessage(messageWithValidAuthor);

        // act
        MessageContent out = testee.parseMessage(input);

        // assert
        assertEquals(expectedAuthor, out.getAuthor());
    }

    @Test
    public void test_parseMessage_authorIsDefaultWhenMessageContainsNoAuthor() {
        final String messageWithoutAuthor = "AuthorIsNotPresent";
        final RawMessage input = new RawMessage(messageWithoutAuthor);

        // act
        MessageContent out = testee.parseMessage(input);

        // assert
        assertEquals(PsMessageParser.DEFAULT_AUTHOR, out.getAuthor());
    }
}