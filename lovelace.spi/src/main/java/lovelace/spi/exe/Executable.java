package lovelace.spi.exe;

import lovelace.spi.console.Console;

/**
 * @author nasser
 */
@FunctionalInterface
public interface Executable {

    void execute(Console console);
}
