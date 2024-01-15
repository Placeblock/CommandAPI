package de.codelix.commandapi.paper;

import de.codelix.commandapi.adventure.AdventureDesign;
import de.codelix.commandapi.adventure.AdventureMessages;
import de.codelix.commandapi.paper.tree.builder.impl.DefaultPaperArgumentBuilder;
import de.codelix.commandapi.paper.tree.builder.impl.DefaultPaperFactory;
import de.codelix.commandapi.paper.tree.builder.impl.DefaultPaperLiteralBuilder;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("unused")
public abstract class DefaultPaperCommand<P> extends PaperCommand<P, DefaultPaperLiteralBuilder<PaperSource<P>, P>, DefaultPaperArgumentBuilder<?, PaperSource<P>, P>> {
    public DefaultPaperCommand(Plugin plugin, String label, boolean async, AdventureDesign<PaperSource<P>> design) {
        super(plugin, label, async, design, new DefaultPaperFactory<>());
    }
    public DefaultPaperCommand(Plugin plugin, String label, AdventureDesign<PaperSource<P>> design) {
        super(plugin, label, true, design, new DefaultPaperFactory<>());
    }
    public DefaultPaperCommand(Plugin plugin, String label, boolean async) {
        super(plugin, label, async, new AdventureDesign<>(new AdventureMessages()), new DefaultPaperFactory<>());
    }
    public DefaultPaperCommand(Plugin plugin, String label) {
        super(plugin, label, true, new AdventureDesign<>(new AdventureMessages()), new DefaultPaperFactory<>());
    }

    protected DefaultPaperLiteralBuilder<PaperSource<P>, P> createLiteralBuilder(String label) {
        return new DefaultPaperLiteralBuilder<>(label);
    }
}
