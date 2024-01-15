package de.codelix.commandapi.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import de.codelix.commandapi.adventure.AdventureDesign;
import de.codelix.commandapi.adventure.AdventureMessages;
import de.codelix.commandapi.velocity.tree.builder.impl.DefaultVelocityArgumentBuilder;
import de.codelix.commandapi.velocity.tree.builder.impl.DefaultVelocityFactory;
import de.codelix.commandapi.velocity.tree.builder.impl.DefaultVelocityLiteralBuilder;

@SuppressWarnings("unused")
public abstract class DefaultVelocityCommand<P> extends VelocityCommand<P, DefaultVelocityLiteralBuilder<VelocitySource<P>, P>, DefaultVelocityArgumentBuilder<?, VelocitySource<P>, P>> {
    public DefaultVelocityCommand(ProxyServer proxy, String label, AdventureDesign<VelocitySource<P>> design) {
        super(proxy, label, design, new DefaultVelocityFactory<>());
    }
    public DefaultVelocityCommand(ProxyServer proxy, String label) {
        super(proxy, label, new AdventureDesign<>(new AdventureMessages()), new DefaultVelocityFactory<>());
    }

    protected DefaultVelocityLiteralBuilder<VelocitySource<P>, P> createLiteralBuilder(String label) {
        return new DefaultVelocityLiteralBuilder<>(label);
    }
}
