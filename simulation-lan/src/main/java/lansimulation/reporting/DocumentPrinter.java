package lansimulation.reporting;

import lansimulation.internals.Node;
import lansimulation.internals.Packet;
import lansimulation.internals.Printer;

public class DocumentPrinter implements IDocumentPrinter {

    private final IMessageAdapter messageAdapter;

    public DocumentPrinter(IMessageAdapter messageAdapter){

        this.messageAdapter = messageAdapter;
    }

    public void printDocument(Printer printer, Packet document, ReportingWrapper report) {
        executePrintJob(document, report);
    }

    private static void logErrorForInvalidDeviceType(ReportingWrapper report) {
        report.write(">>> Destinition is not a printer, print job cancelled.\n\n");
        report.flush();
    }

    private void executePrintJob(Packet document, ReportingWrapper report) {
        final RawMessage message = convertIntoRawMessage(document);
        final MessageContent messageContent = messageAdapter.adaptMessage(message);
        logExecutedAction(report, messageContent);
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

    private void logExecutedAction(ReportingWrapper report, MessageContent messageContent) {
        report.write("\tAccounting -- author = '");
        report.write(messageContent.getAuthor());
        report.write("' -- title = '");
        report.write(messageContent.getTitle());
        report.write("'\n");
        report.write(">>> " + messageContent.getJobType() + " job delivered.\n\n");
        report.flush();
    }
}
