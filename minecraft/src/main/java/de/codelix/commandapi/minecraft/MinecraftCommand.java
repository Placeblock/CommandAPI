package de.codelix.commandapi.minecraft;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.exception.*;
import de.codelix.commandapi.minecraft.tree.builder.impl.DefaultMinecraftArgumentBuilder;
import de.codelix.commandapi.minecraft.tree.builder.impl.DefaultMinecraftLiteralBuilder;

import java.util.List;

@SuppressWarnings("unused")
public interface MinecraftCommand<S extends MinecraftSource<P, C>, P, C, M, D extends MinecraftDesign<S, M>> extends Command<DefaultMinecraftLiteralBuilder<S, P, C>, DefaultMinecraftArgumentBuilder<?, S, P, C>, S, M, D> {
    void build(DefaultMinecraftLiteralBuilder<S, P, C> builder);

    void register();

    void unregister();

    @Override
    default void runSafe(List<String> input, S source) {
        try {
            this.run(input, source);
        } catch (ParseException e) {
            if (e instanceof EndOfCommandParseException ||
                e instanceof InvalidArgumentParseException ||
                e instanceof InvalidLiteralParseException ||
                e instanceof NoRunParseException ||
                e instanceof NoPermissionParseException) {
                this.sendMessage(source, this.getDesign().getHelpMessage(this, source));
                return;
            }
            M message = this.getDesign().getMessages().getMessage(e);
            if (message == null) return;
            this.sendMessage(source, message);
        }
    }
}
