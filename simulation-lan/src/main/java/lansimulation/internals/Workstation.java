package lansimulation.internals;

public class Workstation implements NetworkElement {

    private final String name;
    private NetworkElement nextElement;

    public Workstation(String name) {

        this.name = name;
    }
    public Workstation(String name, NetworkElement nextElement) {
        this.name = name;
        this.nextElement = nextElement;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setNextElement(NetworkElement nextElement) {
        this.nextElement = nextElement;
    }

    @Override
    public NetworkElement getNextElement() {
        return nextElement;
    }

    @Override
    public String getElementDescription() {
        return "Workstation " + name + " [Workstation]";
    }

    @Override
    public boolean isWorkstation() {
        return true;
    }

    @Override
    public boolean isNode() {
        return false;
    }

    @Override
    public boolean isPrinter() {
        return false;
    }

    @Override
    public String getXmlDescription() {
        return "<workstation>" + name + "</workstation>";
    }
}
