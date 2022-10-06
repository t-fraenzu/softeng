package lansimulation;

import lansimulation.internals.NetworkElement;

public class NetworkStatePrinting {


    /**
     * Write a printable representation of #receiver on the given #buf.
     * <p>
     * <strong>Precondition:</strong> isInitialized();
     * </p>
     */
    public void printOn(StringBuffer buf, NetworkElement startNode) {
        NetworkElement currentNode = startNode;
        do {
            buf.append(currentNode.getElementDescription());
            buf.append(" -> ");
            currentNode = currentNode.getNextElement();
        } while (currentNode != startNode);
        buf.append(" ... ");
    }

    /**
     * Write a HTML representation of #receiver on the given #buf.
     * <p>
     * <strong>Precondition:</strong> isInitialized();
     * </p>
     */
    public void printHTMLOn(StringBuffer buf, NetworkElement startNode) {
        buf.append("<HTML>\n<HEAD>\n<TITLE>LAN Simulation</TITLE>\n</HEAD>\n<BODY>\n<H1>LAN SIMULATION</H1>");
        NetworkElement currentNode = startNode;
        buf.append("\n\n<UL>");
        do {
            buf.append("\n\t<LI> ");
            buf.append(currentNode.getElementDescription());
            buf.append(" </LI>");
            currentNode = currentNode.getNextElement();
        } while (currentNode != startNode);
        buf.append("\n\t<LI>...</LI>\n</UL>\n\n</BODY>\n</HTML>\n");
    }

    /**
     * Write an XML representation of #receiver on the given #buf.
     * <p>
     * <strong>Precondition:</strong> isInitialized();
     * </p>
     */
    public void printXMLOn(StringBuffer buf, NetworkElement startNode) {
        NetworkElement currentNode = startNode;
        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<network>");
        do {
            buf.append("\n\t");
            buf.append(currentNode.getXmlDescription());
            currentNode = currentNode.getNextElement();
        } while (currentNode != startNode);
        buf.append("\n</network>");
    }
}
