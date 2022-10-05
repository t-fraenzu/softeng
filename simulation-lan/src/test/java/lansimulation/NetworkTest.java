package lansimulation;

import lansimulation.reporting.IDocumentPrinter;
import lansimulation.reporting.ReportingWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NetworkTest {

    private Network testee;

    @BeforeEach
    public void setUp(){
        testee = new Network(1, Mockito.mock(IDocumentPrinter.class));
    }


    @Test
    public void test_requestBroadcast_sendsMessageToAll(){
        StringWriter stringWriter = new StringWriter();
        ReportingWrapper report = new ReportingWrapper(stringWriter);
        Network network = Network.DefaultExample();

        // act
        network.requestBroadcast(report);

        //assert
        String broadcastOutput = stringWriter.toString();
        String receivedMessage = "'%s' accepts broadcase";
        int indexOfLogForFilip = broadcastOutput.indexOf(String.format(receivedMessage, "Filip"));
        int indexOfLogForN1= broadcastOutput.indexOf(String.format(receivedMessage, "n1"));
        int indexOfLogForHans = broadcastOutput.indexOf(String.format(receivedMessage, "Hans"));
        int indexOfLogForAndy = broadcastOutput.indexOf(String.format(receivedMessage, "Andy"));

        assertTrue(indexOfLogForFilip > 0, "Filip has not received Broadcast message");
        assertTrue(indexOfLogForN1 > 0, "Hans has not received Broadcast message");
        assertTrue(indexOfLogForHans > 0, "N1 has not received Broadcast message");
        assertTrue(indexOfLogForAndy > 0, "Andy has not received Broadcast message");
    }

    @Test
    public void test_hasWorkstation_returnsTrue_ifNodeIsKnownAndAWorkstation(){
        Network network = Network.DefaultExample();

        // act & assert
        assertTrue(network.hasWorkstation("Filip"));
    }

    @Test
    public void test_hasWorkstation_returnsFalse_ifNodeIsKnownButNotAWorkstation(){
        Network network = Network.DefaultExample();

        // act & assert
        assertFalse(network.hasWorkstation("Andy"));
    }

    @Test
    public void test_hasWorkstation_returnsFalse_ifNodeIsUnknown(){
        Network network = Network.DefaultExample();

        // act & assert
        assertFalse(network.hasWorkstation("inexistentNodeInNetwork"));
    }

}