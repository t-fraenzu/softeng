package lansimulation.reporting;

import lansimulation.internals.Node;
import lansimulation.internals.Packet;

import java.io.IOException;
import java.io.Writer;

public class DocumentPrinter implements IDocumentPrinter {

    private final IMessageAdapter messageAdapter;

    public DocumentPrinter(IMessageAdapter messageAdapter){

        this.messageAdapter = messageAdapter;
    }

    public boolean printDocument(Node printer, Packet document, Writer report) {
        if (printer.type_ == Node.PRINTER) {
            executePrintJob(document, report);
            return true;
        }

        logErrorForInvalidDeviceType(report);
        return false;
    }

    private static void logErrorForInvalidDeviceType(Writer report) {
        try {
            report.write(">>> Destinition is not a printer, print job cancelled.\n\n");
            report.flush();
        } catch (IOException exc) {
            // just ignore
        }
    }

    private void executePrintJob(Packet document, Writer report) {
        try {
            final RawMessage message = convertIntoRawMessage(document);
            final MessageContent messageContent = messageAdapter.adaptMessage(message);
            logExecutedAction(report, messageContent);
        } catch (IOException exc) {
            // just ignore
        }
    }

    private static RawMessage convertIntoRawMessage(Packet document) {
        final RawMessage message;
        if (document.message_.startsWith("!PS")) {
            message = new RawMessage(document.message_, MessageType.PS);
        } else {
            message = new RawMessage(document.message_, MessageType.ASCII);
        }
        return message;
    }

    private void logExecutedAction(Writer report, MessageContent messageContent) throws IOException {
        report.write("\tAccounting -- author = '");
        report.write(messageContent.getAuthor());
        report.write("' -- title = '");
        report.write(messageContent.getTitle());
        report.write("'\n");
        report.write(">>> " + messageContent.getJobType() + " job delivered.\n\n");
        report.flush();
    }
}
