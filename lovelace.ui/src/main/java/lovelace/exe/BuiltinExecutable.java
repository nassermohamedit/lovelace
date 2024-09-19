package lovelace.exe;

import lovelace.spi.exe.Executable;

/**
 * @author nasser
 */
public interface BuiltinExecutable extends Executable {

    boolean runOnDispatchThread();
}
