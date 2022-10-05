package lansimulation.reporting;

import java.io.IOException;
import java.io.Writer;

public class ReportingWrapper {

    private final Writer writer;

    public ReportingWrapper(Writer writer){

        this.writer = writer;
    }

    public void write(String message) {
        try {
            writer.write(message);
        } catch (IOException e) {
            // NOOP
        }
    }

    public void flush() {
        try {
            writer.flush();
        } catch (IOException e) {
            // NOOP
        }
    }

    @Override
    public String toString() {
        return writer.toString();
    }
}
