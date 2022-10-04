package lansimulation;

import lansimulation.internals.Node;
import lansimulation.internals.Packet;
import lansimulation.reporting.MessageAdapter;
import lansimulation.reporting.ReportingWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NetworkTest {

    public static final String ANY_DESTINATION = "printerXY";
    private StringWriter writer;
    private static final String ANY_NODENAME = "anyName";
    private Network testee;

    @BeforeEach
    public void setUp() {
        testee = new Network(1, new MessageAdapter(MessageAdapter.DEFAULT_REGISTRY), new ReportingWrapper(null));
        writer = new StringWriter();
    }

    @Test
    public void test_printDocument_AuthorIsWrittenTo_Writer() {
        String anyDestination = "printerXY";
        String expectedAuthor = "ExpectedAuthor";
        String messageWithValidAuthor = "!PSSomeOtherTextauthor:" + expectedAuthor;
        Packet documentMock = new Packet(messageWithValidAuthor, anyDestination);

        Node printerNode = new Node(Node.PRINTER, "anyName");

        // act
        testee.printDocument(printerNode, documentMock, writer);

        // assert
        String printedReport = writer.toString();
        assertTrue(printedReport.contains(expectedAuthor));
    }

    @Test
    public void test_printDocument_titleIsWrittenTo_Writer() {
        final String expectedTitle = "ExpectedTitleValue";
        final String messageWithValidTitle = "!PSSomeOtherTexttitle:" + expectedTitle;
        final Packet documentMock = new Packet(messageWithValidTitle, ANY_DESTINATION);

        Node printerNode = new Node(Node.PRINTER, ANY_NODENAME);

        // act
        testee.printDocument(printerNode, documentMock, writer);

        // assert
        String printedReport = writer.toString();
        assertTrue(printedReport.contains(expectedTitle));
    }

    @Test
    public void test_printDocument_AsciiAuthorIsWrittenTo_Writer() {
        final String expectedAuthor = "Expected";
        final String messageWithValidTitle = "AuthorA>" + expectedAuthor + "<test";
        final Packet documentMock = new Packet(messageWithValidTitle, ANY_DESTINATION);

        Node printerNode = new Node(Node.PRINTER, ANY_NODENAME);

        // act
        testee.printDocument(printerNode, documentMock, writer);

        // assert
        String printedReport = writer.toString();
        assertTrue(printedReport.contains(expectedAuthor));
    }

    @Test
    public void test_printDocument_resolvesAsciiTitle() {
        final String expectedDocumentTitle = "ASCII DOCUMENT";
        final Packet documentMock = new Packet("anyMessageText", ANY_DESTINATION);

        Node printerNode = new Node(Node.PRINTER, ANY_NODENAME);

        // act
        testee.printDocument(printerNode, documentMock, writer);

        // assert
        String printedReport = writer.toString();
        assertTrue(printedReport.contains(expectedDocumentTitle));
    }

}