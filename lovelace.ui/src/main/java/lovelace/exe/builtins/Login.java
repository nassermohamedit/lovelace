package lovelace.exe.builtins;

import lovelace.spi.console.Console;
import lovelace.exe.BuiltinExecutable;
import lovelace.spi.console.Input;
import lovelace.spi.console.Output;

import java.io.IOException;

/**
 * @author nasser
 */
public class Login implements BuiltinExecutable {

    private final String password;

    public Login(String password) {
        this.password = password;
    }

    @Override
    public boolean runOnDispatchThread() {
        return false;
    }


    @Override
    public void execute(Console console) {
        Input in = console.in();
        Output out = console.out();
        out.println("Please enter your credentials");
        out.print("username: ");
        String username, password;
        try {
            username = in.read();
            out.print("password: ");
            password = in.read();
            if (!this.password.equals(password)) {
                out.println("Bad credentials");
            } else {
                out.println("You're logged in");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
