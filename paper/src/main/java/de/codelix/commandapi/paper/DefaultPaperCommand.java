package de.codelix.commandapi.paper;

import de.codelix.commandapi.adventure.AdventureDesign;
import de.codelix.commandapi.adventure.AdventureMessages;
import de.codelix.commandapi.paper.tree.builder.impl.DefaultPaperArgumentBuilder;
import de.codelix.commandapi.paper.tree.builder.impl.DefaultPaperFactory;
import de.codelix.commandapi.paper.tree.builder.impl.DefaultPaperLiteralBuilder;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("unused")
public abstract class DefaultPaperCommand<S extends PaperSource<P>, P> extends PaperCommand<S, P, DefaultPaperLiteralBuilder<S, P>, DefaultPaperArgumentBuilder<?, S, P>> {
    public DefaultPaperCommand(Plugin plugin, String label, boolean async, AdventureDesign<S> design) {
        super(plugin, label, async, design, new DefaultPaperFactory<>());
    }
    public DefaultPaperCommand(Plugin plugin, String label, AdventureDesign<S> design) {
        super(plugin, label, true, design, new DefaultPaperFactory<>());
    }
    public DefaultPaperCommand(Plugin plugin, String label, boolean async) {
        super(plugin, label, async, new AdventureDesign<>(new AdventureMessages()), new DefaultPaperFactory<>());
    }
    public DefaultPaperCommand(Plugin plugin, String label) {
        super(plugin, label, true, new AdventureDesign<>(new AdventureMessages()), new DefaultPaperFactory<>());
    }

    protected DefaultPaperLiteralBuilder<S, P> createLiteralBuilder(String label) {
        return new DefaultPaperLiteralBuilder<>(label);
    }
}
