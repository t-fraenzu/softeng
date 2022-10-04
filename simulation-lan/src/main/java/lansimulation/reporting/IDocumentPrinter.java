package lansimulation.reporting;

import lansimulation.internals.Node;
import lansimulation.internals.Packet;

import java.io.Writer;

public interface IDocumentPrinter {
    boolean printDocument(Node printer, Packet document, Writer report);
}
