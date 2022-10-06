package lansimulation.internals;

public interface NetworkElement {

    String getName();

    void setNextElement(NetworkElement nextElement);

    NetworkElement getNextElement();

    String getElementDescription();

    boolean isWorkstation();

    boolean isNode();

    boolean isPrinter();

    String getXmlDescription();
}

