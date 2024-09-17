package lovelace.exe.builtins;

import lovelace.Console;
import lovelace.exe.BuiltinExecutable;

/**
 * @author nasser
 */
public class Clear implements BuiltinExecutable {

    @Override
    public boolean runOnDispatchThread() {
        return true;
    }

    @Override
    public void execute(Console console) {
        console.clear();
    }
}
