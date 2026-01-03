package de.codelix.commandapi.paper.dtogenerator;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.exception.InvalidArgumentParseException;
import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.paper.PaperSource;
import de.codelix.commandapi.paper.tree.PaperArgument;
import de.codelix.commandapi.paper.tree.impl.PaperNodeImpl;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;

import java.util.Collection;
import java.util.List;

@Getter
public class CallbackArgumentImpl<T, S extends PaperSource<P>, P> extends PaperNodeImpl<S, P> implements PaperArgument<T, S, P> {
    private final String name;
    private final Parameter<T, S, TextComponent> parameter;
    private final T defaultValue;
    @FunctionalInterface
    public interface Consumer<T, S extends PaperSource<P>, P> {
        void accept(ParseContext<S, TextComponent> ctx, ParsedCommand<S, TextComponent> cmd, T data);
    }
    private final Consumer<T, S, P> callback;

    public CallbackArgumentImpl(String name, Parameter<T, S, TextComponent> parameter, T defaultValue, String displayName,
                                String description, List<Node<S, TextComponent>> children, String permission, boolean unsafePermission,
                                boolean optional, Collection<RunConsumer> runConsumers,
                                Consumer<T, S, P> callback) {
        super(displayName, description, children, permission, unsafePermission, optional, runConsumers);
        this.name = name;
        this.parameter = parameter;
        this.defaultValue = defaultValue;
        this.callback = callback;
    }

    @Override
    public void parse(ParseContext<S, TextComponent> ctx, ParsedCommand<S, TextComponent> cmd) throws ParseException {
        T value = this.getParameter().parse(ctx, cmd);
        if (value != null) {
            cmd.storeArgument(this, value);
        } else {
            if (this.isOptional()) {
                cmd.storeArgument(this, this.getDefaultValue());
            }
            throw new InvalidArgumentParseException(this);
        }
        this.callback.accept(ctx, cmd, value);
    }
}
