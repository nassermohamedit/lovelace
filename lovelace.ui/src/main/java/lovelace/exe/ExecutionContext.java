package lovelace.exe;

import lovelace.spi.console.Console;
import lovelace.spi.exe.Executable;

/**
 * @author nasser
 */
public class ExecutionContext {

    protected final Console console;

    public ExecutionContext(Console console) {
        this.console = console;
    }

    final public Console console() {
        return console;
    }

    public final Runnable wrap(Executable exe) {
        return () -> {
            console.begin();
            exe.execute(console);
            console.end();
        };
    }
}
