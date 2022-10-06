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
package lansimulation.integration;

import lansimulation.Network;
import lansimulation.NetworkStatePrinting;
import lansimulation.internals.Node;
import lansimulation.internals.Packet;
import lansimulation.reporting.ReportingWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class LANTests {

    @Test
    public void testBasicPacket() {
        Packet packet;

        packet = new Packet("c", "a");
        assertEquals(packet.message_, "c", "message_");
        assertEquals(packet.destination_, "a", "destination_");
        assertEquals(packet.origin_, "", "origin_");
        packet.origin_ = "o";
        assertEquals(packet.origin_, "o", "origin_ (after setting)");
    }

    private boolean compareFiles(String filename1, String filename2) {
        FileInputStream f1, f2;
        int b1 = 0, b2 = 0;

        try {
            f1 = new FileInputStream(filename1);
            try {
                f2 = new FileInputStream(filename2);
            } catch (FileNotFoundException f2exc) {
                try {
                    f1.close();
                } catch (IOException exc) {
                }
                return false; // file 2 does not exist
            }
        } catch (FileNotFoundException f1exc) {
            return false; // file 1 does not exist
        }

        try {
            if (f1.available() != f2.available()) {
                return false;
            } // length of files is different
            while ((b1 != -1) & (b2 != -1)) {
                b1 = f1.read();
                b2 = f2.read();
                if (b1 != b2) {
                    return false;
                } // discovered one diferring character
            }

            if ((b1 == -1) & (b2 == -1)) {
                return true; // reached both end of files
            } else {
                return false; // one end of file not reached
            }
        } catch (IOException exc) {
            return false; // read error, assume one file corrupted
        } finally {
            try {
                f1.close();
            } catch (IOException exc) {
            }

            try {
                f2.close();
            } catch (IOException exc) {
            }
        }
    }

    @Test
    public void testDefaultNetworkToString() {
        Network network = Network.DefaultExample();

        assertTrue(network.isInitialized(), "isInitialized ");
        assertTrue(network.consistentNetwork(), "consistentNetwork ");
        assertEquals(
                network.toString(),
                "Workstation Filip [Workstation] -> Node n1 [Node] -> Workstation Hans [Workstation] -> Printer Andy [Printer] ->  ... ",
                "DefaultNetwork.toString()");
    }

    @Test
    public void testWorkstationPrintsDocument() {
        Network network = Network.DefaultExample();
        ReportingWrapper report = new ReportingWrapper(new StringWriter(500));

        assertTrue(network.requestWorkstationPrintsDocument(
                "Filip", "Hello World", "Andy", report), "PrintSuccess ");
        assertFalse(network
                .requestWorkstationPrintsDocument("Filip", "Hello World",
                        "UnknownPrinter", report), "PrintFailure (UnkownPrinter) ");
        assertFalse(network
                .requestWorkstationPrintsDocument("Filip", "Hello World",
                        "Hans", report), "PrintFailure (print on Workstation) ");
        assertFalse(network
                .requestWorkstationPrintsDocument("Filip", "Hello World", "n1",
                        report), "PrintFailure (print on Node) ");
        assertTrue(network.requestWorkstationPrintsDocument("Filip",
                "!PS Hello World in postscript", "Andy", report), "PrintSuccess Postscript");
        assertFalse(network
                .requestWorkstationPrintsDocument("Filip",
                        "!PS Hello World in postscript", "Hans", report),
                "PrintFailure Postscript");
    }

    @Test
    public void testBroadcast() {
        Network network = Network.DefaultExample();
        ReportingWrapper report = new ReportingWrapper(new StringWriter(500));

        assertTrue(network.requestBroadcast(report), "Broadcast ");
    }

    /**
     * Test whether output routines work as expected. This is done by comparing
     * generating output on a file "useOutput.txt" and comparing it to a file
     * "expectedOutput.txt". On a first run this test might break because the
     * file "expectedOutput.txt" does not exist. Then just run the test, verify
     * manually whether "useOutput.txt" conforms to the expected output and if
     * it does rename "useOutput.txt" in "expectedOutput.txt". From then on the
     * tests should work as expected.
     */
    @Test
    public void testOutput() {
        Network network = Network.DefaultExample();
        String generateOutputFName = "useOutput.txt", expectedOutputFName = "expectedOutput.txt";
        FileWriter generateOutput;
        StringBuffer buf = new StringBuffer(500);
        ReportingWrapper report = new ReportingWrapper(new StringWriter(500));

        try {
            generateOutput = new FileWriter(generateOutputFName);
        } catch (IOException f2exc) {
            assertTrue(false, "Could not create '" + generateOutputFName + "'");
            return;
        }
        NetworkStatePrinting nsp = new NetworkStatePrinting();
        try {

            buf.append("---------------------------------ASCII------------------------------------------\n");
            nsp.printOn(buf, network.getStartElement());
            buf.append("\n\n---------------------------------HTML------------------------------------------\n");
            nsp.printHTMLOn(buf, network.getStartElement());
            buf.append("\n\n---------------------------------XML------------------------------------------\n");
            nsp.printXMLOn(buf, network.getStartElement());
            generateOutput.write(buf.toString());

            report.write("\n\n---------------------------------SCENARIO: Print Success --------------------------\n");
            network.requestWorkstationPrintsDocument("Filip", "Hello World",
                    "Andy", report);
            report.write("\n\n---------------------------------SCENARIO: PrintFailure (UnkownPrinter) ------------\n");
            network.requestWorkstationPrintsDocument("Filip", "Hello World",
                    "UnknownPrinter", report);
            report.write("\n\n---------------------------------SCENARIO: PrintFailure (print on Workstation) -----\n");
            network.requestWorkstationPrintsDocument("Filip", "Hello World",
                    "Hans", report);
            report.write("\n\n---------------------------------SCENARIO: PrintFailure (print on Node) -----\n");
            network.requestWorkstationPrintsDocument("Filip", "Hello World",
                    "n1", report);
            report.write("\n\n---------------------------------SCENARIO: Print Success Postscript-----------------\n");
            network.requestWorkstationPrintsDocument("Filip",
                    "!PS Hello World in postscript", "Andy", report);
            report.write("\n\n---------------------------------SCENARIO: Print Failure Postscript-----------------\n");
            network.requestWorkstationPrintsDocument("Filip",
                    "!PS Hello World in postscript", "Hans", report);
            report.write("\n\n---------------------------------SCENARIO: Broadcast Success -----------------\n");
            network.requestBroadcast(report);
            generateOutput.write(report.toString());
        } catch (IOException exc) {
        } finally {
            try {
                generateOutput.close();
            } catch (IOException exc) {
            }
        }
        assertTrue(compareFiles(
                generateOutputFName, expectedOutputFName), "Generated output is not as expected ");
    }

    @Test
    public void testPreconditionViolation() {
        // arrange
        Network network = Network.DefaultExample();
        ReportingWrapper report = new ReportingWrapper(new StringWriter(500));
        String unknownWorkstation = "StrangeWorkstationName";
        String anyString = "randomText";

        // act & assert
        Assertions.assertThrows(AssertionError.class,
                () -> network.requestWorkstationPrintsDocument(unknownWorkstation, anyString, anyString, report));
    }
}