package de.placeblock.commandapi.core.parser;

import de.placeblock.commandapi.core.parameter.Parameter;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;
import de.placeblock.commandapi.core.tree.TreeCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.TextComponent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Placeblock
 */
@Getter
@RequiredArgsConstructor
public class ParseContext<S> {

    private final String text;
    private final S source;
    private final Map<String, Object> parameters = new HashMap<>();
    @Setter
    private Map<Parameter<S, ?>, TextComponent> errors = new HashMap<>();
    @Setter
    private TreeCommand<S> lastParsedCommand;
    @Setter
    private int cursor = 0;

    public void addParameter(String name, Object value) {
        this.parameters.put(name, value);
    }

    public <T> T getParameter(String name, Class<T> type) {
        return type.cast(this.parameters.get(name));
    }

    public TextComponent getLastParsedError() {
        if (this.lastParsedCommand == null) return null;
        if (this.lastParsedCommand instanceof ParameterTreeCommand<S, ?> parameterTreeCommand && this.getErrors().containsKey(parameterTreeCommand.getParameter())) {
            return this.getErrors().get(parameterTreeCommand.getParameter());
        }
        return null;
    }

}
