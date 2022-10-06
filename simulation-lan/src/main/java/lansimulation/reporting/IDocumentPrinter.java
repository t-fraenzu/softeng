package lansimulation.reporting;

import lansimulation.internals.Packet;
import lansimulation.internals.Printer;

public interface IDocumentPrinter {
    void printDocument(Printer printer, Packet document, ReportingWrapper report);
}
