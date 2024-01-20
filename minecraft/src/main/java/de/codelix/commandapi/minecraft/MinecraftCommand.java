package de.codelix.commandapi.minecraft;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.exception.*;
import de.codelix.commandapi.minecraft.tree.builder.MinecraftArgumentBuilder;
import de.codelix.commandapi.minecraft.tree.builder.MinecraftLiteralBuilder;

import java.util.List;

@SuppressWarnings("unused")
public interface MinecraftCommand<S extends MinecraftSource<P, C, M>, P, C, M, D extends MinecraftDesign<S, M>, L extends MinecraftLiteralBuilder<?, ?, S, P, C, M>, A extends MinecraftArgumentBuilder<?, ?, ?, S, P, C, M>> extends Command<S, M, D, L, A> {
    void build(L builder);

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
                source.sendMessage(this.getDesign().getHelpMessage(this, source));
                return;
            }
            M message = this.getDesign().getMessages().getMessage(e);
            if (message == null) return;
            source.sendMessage(message);
        }
    }
}
