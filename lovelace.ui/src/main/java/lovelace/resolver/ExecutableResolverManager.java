package lovelace.resolver;

import lovelace.spi.exe.Executable;

import java.util.List;
import java.util.Optional;

/**
 * @author nasser
 */
public class ExecutableResolverManager implements ExecutableResolver {

    private final List<ExecutableResolver> resolvers;

    public ExecutableResolverManager(List<ExecutableResolver> resolvers) {
        this.resolvers = resolvers;
    }

    public void addResolver(ExecutableResolver resolver) {
        resolvers.add(resolver);
    }

    public void removeResolver(ExecutableResolver resolver) {
        resolvers.remove(resolver);
    }

    @Override
    public boolean canResolve(String name) {
        for (var resolver : resolvers) {
            boolean can = false;
            if (resolver.canResolve(name))
                return true;
        }
        return false;
    }

    @Override
    public Optional<? extends Executable> resolve(String name) {
        for (var resolver : resolvers) {
            boolean possible = true;
            try {
                possible = resolver.canResolve(name);
            } catch (UnsupportedOperationException ignored) {}
            if (possible) {
                Optional<? extends Executable> exe = resolver.resolve(name);
                if (exe.isPresent()) {
                    return exe;
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public String description() {
        return "";
    }
}
