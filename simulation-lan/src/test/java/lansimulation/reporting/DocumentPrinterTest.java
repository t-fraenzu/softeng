package lansimulation.reporting;

import lansimulation.internals.Packet;
import lansimulation.internals.Printer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DocumentPrinterTest {

    public static final String ANY_DESTINATION = "printerXY";
    private StringWriter writer;
    private static final String ANY_NODENAME = "anyName";
    private DocumentPrinter testee;

    @BeforeEach
    public void setUp() {
        testee = new DocumentPrinter(new MessageAdapter(MessageAdapter.DEFAULT_REGISTRY));
        writer = new StringWriter();
    }

    @Test
    public void test_printDocument_AuthorIsWrittenTo_Writer() {
        String anyDestination = "printerXY";
        String expectedAuthor = "ExpectedAuthor";
        String messageWithValidAuthor = "!PSSomeOtherTextauthor:" + expectedAuthor;
        Packet documentMock = new Packet(messageWithValidAuthor, anyDestination);

        Printer printerNode = new Printer("anyName", null);

        // act
        testee.printDocument(printerNode, documentMock, new ReportingWrapper(writer));

        // assert
        String printedReport = writer.toString();
        assertTrue(printedReport.contains(expectedAuthor));
    }

    @Test
    public void test_printDocument_titleIsWrittenTo_Writer() {
        final String expectedTitle = "ExpectedTitleValue";
        final String messageWithValidTitle = "!PSSomeOtherTexttitle:" + expectedTitle;
        final Packet documentMock = new Packet(messageWithValidTitle, ANY_DESTINATION);

        Printer printerNode = new Printer(ANY_NODENAME, null);

        // act
        testee.printDocument(printerNode, documentMock, new ReportingWrapper(writer));

        // assert
        String printedReport = writer.toString();
        assertTrue(printedReport.contains(expectedTitle));
    }

    @Test
    public void test_printDocument_AsciiAuthorIsWrittenTo_Writer() {
        final String expectedAuthor = "Expected";
        final String messageWithValidTitle = "AuthorA>" + expectedAuthor + "<test";
        final Packet documentMock = new Packet(messageWithValidTitle, ANY_DESTINATION);

        Printer printerNode = new Printer(ANY_NODENAME, null);

        // act
        testee.printDocument(printerNode, documentMock, new ReportingWrapper(writer));

        // assert
        String printedReport = writer.toString();
        assertTrue(printedReport.contains(expectedAuthor));
    }

    @Test
    public void test_printDocument_resolvesAsciiTitle() {
        final String expectedDocumentTitle = "ASCII DOCUMENT";
        final Packet documentMock = new Packet("anyMessageText", ANY_DESTINATION);

        Printer printerNode = new Printer(ANY_NODENAME, null);

        // act
        testee.printDocument(printerNode, documentMock, new ReportingWrapper(writer));

        // assert
        String printedReport = writer.toString();
        assertTrue(printedReport.contains(expectedDocumentTitle));
    }
}