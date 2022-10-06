package lansimulation.internals;

public class Printer implements NetworkElement {

    private final String name;
    private NetworkElement nextElement;

    public Printer(String name, NetworkElement nextElement) {

        this.name = name;
        this.nextElement = nextElement;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public NetworkElement getNextElement() {
        return nextElement;
    }

    @Override
    public String getElementDescription() {
        return "Printer " + name + " [Printer]";
    }

    @Override
    public boolean isWorkstation() {
        return false;
    }

    @Override
    public boolean isNode() {
        return false;
    }

    @Override
    public boolean isPrinter() {
        return true;
    }

    @Override
    public String getXmlDescription() {
        return "<printer>" + name + "</printer>";
    }

    @Override
    public void setNextElement(NetworkElement nextElement) {
        this.nextElement = nextElement;
    }
}
