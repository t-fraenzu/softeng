package lansimulation.reporting;

public interface IMessageParser {

    MessageContent parseMessage(RawMessage rawMessage);
}
