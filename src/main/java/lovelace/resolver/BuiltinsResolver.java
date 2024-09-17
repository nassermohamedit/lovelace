package lovelace.resolver;

import lovelace.exe.BuiltinExecutable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author nasser
 */
public class BuiltinsResolver implements ExecutableResolver {

    private final Map<String, BuiltinExecutable> exes;

    public BuiltinsResolver() {
        exes = new HashMap<>();
    }

    public void add(String name, BuiltinExecutable exe) {
        exes.put(name, exe);
    }

    @Override
    public boolean canResolve(String name) {
        return exes.containsKey(name);
    }

    @Override
    public Optional<BuiltinExecutable> resolve(String name) {
        return canResolve(name) ? Optional.of(exes.get(name)) : Optional.empty();
    }

    @Override
    public String description() {
        return "";
    }
}
