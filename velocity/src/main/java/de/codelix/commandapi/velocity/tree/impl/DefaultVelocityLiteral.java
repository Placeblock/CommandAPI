package de.codelix.commandapi.velocity.tree.impl;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.velocity.VelocitySource;
import de.codelix.commandapi.velocity.tree.VelocityLiteral;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;

import java.util.Collection;
import java.util.List;

@Getter
public class DefaultVelocityLiteral<S extends VelocitySource<P>, P> extends DefaultVelocityNode<S, P> implements VelocityLiteral<S, P> {
    private final List<String> names;

    public DefaultVelocityLiteral(List<String> names, String displayName, String description, List<Node<S, TextComponent>> children, String permission, boolean unsafePermission, boolean optional, Collection<RunConsumer> runConsumers) {
        super(displayName, description, children, permission, unsafePermission, optional, runConsumers);
        this.names = names;
    }
}
