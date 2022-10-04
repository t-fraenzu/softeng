package lansimulation.reporting;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.EnumMap;
import java.util.Map;

import static org.mockito.Mockito.*;

class MessageAdapterTest {

    @Test
    public void executes_ActionAccordingToMessageType(){
        // arrange
        IMessageParser messageParserMockExpected = Mockito.mock(IMessageParser.class);
        IMessageParser messageParserMockWrong = Mockito.mock(IMessageParser.class);
        RawMessage psMessage = new RawMessage("anyText", MessageType.PS);

        MessageAdapter testee = new MessageAdapter(new EnumMap<MessageType, IMessageParser>(
                Map.of(
                        MessageType.PS, messageParserMockExpected,
                        MessageType.ASCII, messageParserMockWrong)));

        // act
        testee.adaptMessage(psMessage);

        // assert
        verify(messageParserMockExpected, times(1)).parseMessage(psMessage);
        verify(messageParserMockWrong, never()).parseMessage(psMessage);
    }
}