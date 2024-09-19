package lovelace;

import lovelace.exe.BuiltinExecutable;
import lovelace.exe.ExecutionContext;
import lovelace.resolver.ExecutableResolver;
import lovelace.spi.console.Input;
import lovelace.spi.console.Output;
import lovelace.spi.exe.Executable;
import lovelace.spi.console.Console;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author nasser
 */
public final class DefaultConsole extends JTextPane implements Console {

    private final ConsoleReader in = new ConsoleReader();

    private final ConsoleWriter out = new ConsoleWriter(this);

    private ExecutableResolver resolver;

    private final Executor cmdExecutor;

    private final ExecutionContext ectx = new ExecutionContext(this);

    private boolean running = false;

    public DefaultConsole() {
        this(null, null, null);
    }

    public DefaultConsole(ConsoleDocument doc, ExecutableResolver res, Executor ce) {
        super(doc != null ? doc : new ConsoleDocument());
        this.cmdExecutor = ce != null ? ce : Executors.newSingleThreadExecutor();
        this.resolver = res;
        init();
    }

    private void init() {
        insert(getPrompt());
        Action breakAction = new BreakStrokeHandler(this);
        getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "breakAction");
        getActionMap().put("breakAction", breakAction);
    }

    ConsoleDocument getConsoleDocument() {
        return (ConsoleDocument) getDocument();
    }

    private synchronized void setRunning(boolean b) {
        if (running && b) {
            throw new IllegalStateException();
        }
        running = b;
    }

    @Override
    public void begin() {
        setRunning(true);
    }

    @Override
    public void end() {
        setRunning(false);
        insertPrompt();
    }

    void setCommandResolver(ExecutableResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void clear() {
        setDocument(new ConsoleDocument());
    }

    public Input in() {
        return in;
    }

    public Output out() {
        return out;
    }

    private final static class ConsoleReader implements Input {

        private String in;

        private final AtomicBoolean waiting = new AtomicBoolean(false);

        private ConsoleReader() {}

        public String read() throws IOException, InterruptedException {
            if (waiting.compareAndExchange(false, true)) {
                throw new IllegalStateException();
            }
            synchronized (this) {
                in = null;
                this.wait();
                if (in == null) {
                    throw new IOException();
                }
                waiting.set(false);
                return in;
            }
        }

        private synchronized void setInputAndWakeup(String in) {
            if (waiting.get()) {
                this.in = in;
                this.notify();
            }
        }
    }

    private static class ConsoleWriter implements Output {

        private final DefaultConsole console;

        private ConsoleWriter(DefaultConsole console) {
            this.console = console;
        }

        public void print(String s) {
            console.insert(s);
        }

        public void println(String s) {
            console.insert(s + "\n");
        }
    }

    private void insert(String s) {
        getConsoleDocument().insertAndMark(s, null);
    }

    private void insertPrompt() {
        getConsoleDocument().insertAndMark(getPrompt(), null);
    }


    String getPrompt() {
        return "$ ";
    }

    private static final class BreakStrokeHandler extends AbstractAction {

        private final DefaultConsole console;

        private BreakStrokeHandler(DefaultConsole console) {
            this.console = console;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String input = console.getConsoleDocument().getMutText();
            console.insert("\n");
            if (console.running) {
                var in = console.in;
                if (in.waiting.get()) {
                    in.setInputAndWakeup(input);
                }
            } else if (input.isBlank()) {
                console.insertPrompt();
            } else if (console.resolver == null) {
                console.insert(unresolvedErrMsg(input.split(Utils.WHITE_SPACE)[0]));
            }
            else {
                String[] command = Utils.prepareCommand(input);
                Optional<? extends Executable> exeOp = console.resolver.resolve(command[0]);
                if (exeOp.isEmpty()) {
                    console.insert(unresolvedErrMsg(command[0]));
                    console.insertPrompt();
                    return;
                }
                Executable exe = exeOp.get();
                if (exe instanceof BuiltinExecutable builtin) {
                    run(builtin);
                }
                else {
                    Runnable r = console.ectx.wrap(exe);
                    console.cmdExecutor.execute(r);
                }
            }
        }

        void run(BuiltinExecutable builtin) {
            if (builtin.runOnDispatchThread()) {
                builtin.execute(console);
                console.insertPrompt();
            } else {
                Runnable r = console.ectx.wrap(builtin);
                console.cmdExecutor.execute(r);
            }
        }

        private String unresolvedErrMsg(String cmd) {
            return "Unknown command: " + cmd + "\n";
        }
    }
}
