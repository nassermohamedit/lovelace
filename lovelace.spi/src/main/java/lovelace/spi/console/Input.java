package lovelace.spi.console;

import java.io.IOException;

/**
 * @author nasser
 */
public interface Input {

    String read() throws IOException, InterruptedException;
}
