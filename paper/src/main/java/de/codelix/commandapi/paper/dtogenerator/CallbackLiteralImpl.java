package de.codelix.commandapi.paper.dtogenerator;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.exception.InvalidLiteralParseException;
import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.parser.Source;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.paper.PaperSource;
import de.codelix.commandapi.paper.tree.PaperLiteral;
import de.codelix.commandapi.paper.tree.impl.PaperNodeImpl;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

@Getter
public class CallbackLiteralImpl<S extends PaperSource<P>, P> extends PaperNodeImpl<S, P> implements PaperLiteral<S, P> {
    private final List<String> names;
    private final BiConsumer<ParseContext<S, TextComponent>, ParsedCommand<S, TextComponent>> callback;

    public CallbackLiteralImpl(List<String> names, String displayName, String description, List<Node<S, TextComponent>> children,
                               String permission, boolean unsafePermission, boolean optional, Collection<RunConsumer> runConsumers,
                               BiConsumer<ParseContext<S, TextComponent>, ParsedCommand<S, TextComponent>> callback) {
        super(displayName, description, children, permission, unsafePermission, optional, runConsumers);
        this.names = names;
        this.callback = callback;
    }

    @Override
    public void parse(ParseContext<S, TextComponent> ctx, ParsedCommand<S, TextComponent> cmd) throws ParseException {
        String next = ctx.getInput().poll();
        if (!this.getNames().contains(next)) {
            throw new InvalidLiteralParseException(this, next);
        }
        this.callback.accept(ctx, cmd);
    }
}
