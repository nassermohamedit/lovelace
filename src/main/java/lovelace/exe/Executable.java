package lovelace.exe;

import lovelace.Console;

/**
 * @author nasser
 */
@FunctionalInterface
public interface Executable {

    void execute(Console console);
}
