package lovelace.resolver;

import lovelace.exe.Executable;

import java.util.Optional;

/**
 * @author nasser
 */
public interface ExecutableResolver {

    boolean canResolve(String name);

    Optional<? extends Executable> resolve(String name);

    String description();
}
