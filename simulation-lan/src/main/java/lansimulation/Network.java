/*   This file is part of lanSimulation.
 *
 *   lanSimulation is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   lanSimulation is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with lanSimulation; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *   Copyright original Java version: 2004 Bart Du Bois, Serge Demeyer
 *   Copyright C++ version: 2006 Matthias Rieger, Bart Van Rompaey
 */
package lansimulation;

import lansimulation.internals.*;
import lansimulation.reporting.DocumentPrinter;
import lansimulation.reporting.IDocumentPrinter;
import lansimulation.reporting.MessageAdapter;
import lansimulation.reporting.ReportingWrapper;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A <em>Network</em> represents the basic data stucture for simulating a
 * Local Area Network (LAN). The LAN network architecture is a token ring,
 * implying that packahes will be passed from one node to another, until they
 * reached their destination, or until they travelled the whole token ring.
 */
public class Network {
    private final IDocumentPrinter documentPrinter;
    /**
     * Holds a pointer to myself. Used to verify whether I am properly
     * initialized.
     */
    private Network initPtr_;
    /**
     * Holds a pointer to some "first" node in the token ring. Used to ensure
     * that various printing operations return expected behaviour.
     */
    private NetworkElement firstNode_;
    /**
     * Maps the names of workstations on the actual workstations. Used to
     * initiate the requests for the network.
     */
    @SuppressWarnings("unchecked")
    private Map<String, Workstation> workstations_;

    /**
     * Construct a <em>Network</em> suitable for holding #size Workstations.
     * <p>
     * <strong>Postcondition:</strong>(result.isInitialized()) & (!
     * result.consistentNetwork());
     * </p>
     */
    @SuppressWarnings("unchecked")
    public Network(int size, IDocumentPrinter documentPrinter) {
        this.documentPrinter = documentPrinter;
        assert size > 0;
        initPtr_ = this;
        firstNode_ = null;
        workstations_ = new HashMap<>(size, 1.0f);
        assert isInitialized();
        assert !consistentNetwork();
    }

    /**
     * Return a <em>Network</em> that may serve as starting point for various
     * experiments. Currently, the network looks as follows.
     *
     * <pre>
     * 	 Workstation Filip [Workstation] -&gt; Node -&gt; Workstation Hans [Workstation]
     * 	 -&gt; Printer Andy [Printer] -&gt; ...
     *
     * </pre>
     *
     * <p>
     * <strong>Postcondition:</strong>result.isInitialized() &
     * result.consistentNetwork();
     * </p>
     */
    @SuppressWarnings("unchecked")
    public static Network DefaultExample() {
        Network network = new Network(2, new DocumentPrinter(new MessageAdapter(MessageAdapter.DEFAULT_REGISTRY)));

        Printer prAndy = new Printer("Andy", null);
        Workstation wsHans = new Workstation("Hans", prAndy);
        Node n1 = new Node("n1", wsHans);
        Workstation wsFilip = new Workstation("Filip", n1);
        prAndy.setNextElement(wsFilip);

        network.workstations_.put(wsFilip.getName(), wsFilip);
        network.workstations_.put(wsHans.getName(), wsHans);
        network.firstNode_ = wsFilip;

        assert network.isInitialized();
        assert network.consistentNetwork();
        return network;
    }

    /**
     * Answer whether #receiver is properly initialized.
     */
    public boolean isInitialized() {
        return (initPtr_ == this);
    }

    /**
     * Answer whether #receiver contains a workstation with the given name.
     * <p>
     * <strong>Precondition:</strong>this.isInitialized();
     * </p>
     */
    public boolean hasWorkstation(String ws) {
        assert isInitialized();

        return workstations_.get(ws) != null;
    }

    /**
     * Answer whether #receiver is a consistent token ring network. A consistent
     * token ring network - contains at least one workstation and one printer -
     * is circular - all registered workstations are on the token ring - all
     * workstations on the token ring are registered.
     * <p>
     * <strong>Precondition:</strong>this.isInitialized();
     * </p>
     */
    @SuppressWarnings("unchecked")
    public boolean consistentNetwork() {
        assert isInitialized();
        NetworkElement currentNode;
        int printersFound = 0, workstationsFound = 0;
        Hashtable encountered = new Hashtable(workstations_.size() * 2, 1.0f);

        if (workstations_.isEmpty()) {
            return false;
        }

        if (firstNode_ == null) {
            return false;
        }

        // enumerate the token ring, verifying whether all workstations are
        // registered
        // also count the number of printers and see whether the ring is
        // circular
        currentNode = firstNode_;
        while (!encountered.containsKey(currentNode.getName())) {
            encountered.put(currentNode.getName(), currentNode);
            if (currentNode.isWorkstation()) {
                workstationsFound++;
            }

            if (currentNode.isPrinter()) {
                printersFound++;
            }

            currentNode = currentNode.getNextElement();
        }

        if (currentNode != firstNode_) {
            return false;
        }

        // not circular
        if (printersFound == 0) {
            return false;
        }

        // does not contain a printer
        if (workstationsFound != workstations_.size()) {
            return false;
        }

        // not all workstations are registered
        // all verifications succeedeed
        return true;
    }

    /**
     * The #receiver is requested to broadcast a message to all nodes. Therefore
     * #receiver sends a special broadcast packet across the token ring network,
     * which should be treated by all nodes.
     * <p>
     * <strong>Precondition:</strong> consistentNetwork();
     * </p>
     *
     * @param report Stream that will hold a report about what happened when
     *               handling the request.
     * @return Anwer #true when the broadcast operation was succesful and #false
     * otherwise
     */
    public boolean requestBroadcast(ReportingWrapper report) {
        assert consistentNetwork();

        report.write("Broadcast Request\n");

        Packet packet = new Packet("BROADCAST", firstNode_.getName(), firstNode_.getName());
        Consumer<NetworkElement> consumerActionOnHop = node -> {
            logAcceptingBroadcastPacket(report, node);
        };
        sendPacketToDestination(report, firstNode_, packet, consumerActionOnHop);

        report.write(">>> Broadcast travelled whole token ring.\n\n");

        return true;
    }

    private void logAcceptingBroadcastPacket(ReportingWrapper report, NetworkElement currentNode) {
        report.write("\tNode '");
        report.write(currentNode.getName());
        report.write("' accepts broadcase packet.\n");
        report.flush();
    }

    /**
     * The #receiver is requested by #workstation to print #document on
     * #printer. Therefore #receiver sends a packet across the token ring
     * network, until either (1) #printer is reached or (2) the packet travelled
     * complete token ring.
     * <p>
     * <strong>Precondition:</strong> consistentNetwork() &
     * hasWorkstation(workstation);
     * </p>
     *
     * @param workstation Name of the workstation requesting the service.
     * @param document    Contents that should be printed on the printer.
     * @param printer     Name of the printer that should receive the document.
     * @param report      Stream that will hold a report about what happened when
     *                    handling the request.
     * @return Anwer #true when the print operation was succesful and #false
     * otherwise
     */
    public boolean requestWorkstationPrintsDocument(String workstation,
                                                    String document, String printer, ReportingWrapper report) {
        assert consistentNetwork() & hasWorkstation(workstation);

        logRequest(workstation, document, printer, report);

        Packet packet = new Packet(document, workstation, printer);

        final Workstation startNode = workstations_.get(workstation);
        Consumer<NetworkElement> emptyHopAction = node -> {
        };
        final NetworkElement possibleDestinationNode = sendPacketToDestination(report, startNode, packet, emptyHopAction);

        if (atDestination(possibleDestinationNode, packet)) {
            if (possibleDestinationNode.isPrinter()) {
                documentPrinter.printDocument((Printer) possibleDestinationNode, packet, report);
                return true;
            } else {
                logErrorForInvalidDeviceType(report);
                return false;
            }
        }

        logJobCanceled(report);
        return false;
    }

    private static void logErrorForInvalidDeviceType(ReportingWrapper report) {
        report.write(">>> Destinition is not a printer, print job cancelled.\n\n");
        report.flush();
    }

    private static void logJobCanceled(ReportingWrapper report) {
        report.write(">>> Destinition not found, print job cancelled.\n\n");
        report.flush();
    }

    private NetworkElement sendPacketToDestination(ReportingWrapper report, NetworkElement networkElement, Packet packet, Consumer<NetworkElement> consumerActionOnHop) {
        consumerActionOnHop.accept(networkElement);
        logForwardingOfPacket(report, networkElement);
        NetworkElement currentNode = networkElement.getNextElement();
        if (atDestination(currentNode, packet)) {
            return currentNode;
        }

        if (infiniteLoop(packet, currentNode)) {
            return currentNode;
        }

        return sendPacketToDestination(report, currentNode, packet, consumerActionOnHop);
    }

    private static boolean infiniteLoop(Packet packet, NetworkElement currentNode) {
        return packet.origin_.equals(currentNode.getName());
    }

    private boolean atDestination(NetworkElement currentNode, Packet packet) {
        return currentNode.getName().equals(packet.destination_);
    }

    private static void logRequest(String workstation, String document, String printer, ReportingWrapper report) {
        report.write("'");
        report.write(workstation);
        report.write("' requests printing of '");
        report.write(document);
        report.write("' on '");
        report.write(printer);
        report.write("' ...\n");
    }

    private static void logForwardingOfPacket(ReportingWrapper report, NetworkElement startNode) {
        report.write("\tNode '");
        report.write(startNode.getName());
        report.write("' passes packet on.\n");
        report.flush();
    }

    /**
     * Return a printable representation of #receiver.
     * <p>
     * <strong>Precondition:</strong> isInitialized();
     * </p>
     */
    public String toString() {
        assert isInitialized();
        StringBuffer buf = new StringBuffer(30 * workstations_.size());
        printOn(buf);
        return buf.toString();
    }

    /**
     * Write a printable representation of #receiver on the given #buf.
     * <p>
     * <strong>Precondition:</strong> isInitialized();
     * </p>
     */
    public void printOn(StringBuffer buf) {
        assert isInitialized();
        NetworkElement currentNode = firstNode_;
        do {
            buf.append(currentNode.getElementDescription());
            buf.append(" -> ");
            currentNode = currentNode.getNextElement();
        } while (currentNode != firstNode_);
        buf.append(" ... ");
    }

    /**
     * Write a HTML representation of #receiver on the given #buf.
     * <p>
     * <strong>Precondition:</strong> isInitialized();
     * </p>
     */
    public void printHTMLOn(StringBuffer buf) {
        assert isInitialized();

        buf.append("<HTML>\n<HEAD>\n<TITLE>LAN Simulation</TITLE>\n</HEAD>\n<BODY>\n<H1>LAN SIMULATION</H1>");
        NetworkElement currentNode = firstNode_;
        buf.append("\n\n<UL>");
        do {
            buf.append("\n\t<LI> ");
            buf.append(currentNode.getElementDescription());
            buf.append(" </LI>");
            currentNode = currentNode.getNextElement();
        } while (currentNode != firstNode_);
        buf.append("\n\t<LI>...</LI>\n</UL>\n\n</BODY>\n</HTML>\n");
    }

    /**
     * Write an XML representation of #receiver on the given #buf.
     * <p>
     * <strong>Precondition:</strong> isInitialized();
     * </p>
     */
    public void printXMLOn(StringBuffer buf) {
        assert isInitialized();

        NetworkElement currentNode = firstNode_;
        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<network>");
        do {
            buf.append("\n\t");
            buf.append(currentNode.getXmlDescription());
            currentNode = currentNode.getNextElement();
        } while (currentNode != firstNode_);
        buf.append("\n</network>");
    }
}
