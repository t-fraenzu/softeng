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
 *   Foundation, Inc. 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *   Copyright original Java version: 2004 Bart Du Bois, Serge Demeyer
 *   Copyright C++ version: 2006 Matthias Rieger, Bart Van Rompaey
 */
package lansimulation.internals;

/**
 * A <em>Node</em> represents a single Node in a Local Area Network (LAN).
 * Several types of Nodes exist.
 */
public class Node implements NetworkElement {

    /**
     * Holds the name of the Node.
     */
    public String name;

    /**
     * Holds the next Node in the token ring architecture.
     */
    private NetworkElement nextElement;

    /**
     * Construct a <em>Node</em> with given #name, and which is
     * linked to #nextElement.
     * <p>
     * <strong>Precondition:</strong> (type >= NODE) & (type <= PRINTER);
     * </p>
     */
    public Node(String name, NetworkElement nextElement) {
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
        return "Node " + name + " [Node]";
    }

    @Override
    public boolean isWorkstation() {
        return false;
    }

    @Override
    public boolean isNode() {
        return true;
    }

    @Override
    public boolean isPrinter() {
        return false;
    }

    @Override
    public String getXmlDescription() {
        return "<node>" + name + "</node>";
    }
}