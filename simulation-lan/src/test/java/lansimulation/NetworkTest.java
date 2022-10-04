package lansimulation;

import lansimulation.internals.Node;
import lansimulation.internals.Packet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NetworkTest {

    public static final String ANY_DESTINATION = "printerXY";
    private StringWriter writer;
    private static final String ANY_NODENAME = "anyName";
    private Network testee;

    @BeforeEach
    public void setUp(){
        testee = new Network(1);
        writer = new StringWriter();
    }

    @Test
    void test_printDocument_AuthorIsWrittenTo_Writer(){
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
    void test_printDocument_titleIsWrittenTo_Writer(){
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

}