package de.codelix.commandapi.core.parser;

import de.codelix.commandapi.core.tree.ParameterCommandNode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public abstract class ParameterHolder {
    @Getter
    protected final Map<ParameterCommandNode<?, ?>, Object> parsedParameters;

    public ParameterHolder() {
        this(new HashMap<>());
    }

    public ParameterHolder(Map<ParameterCommandNode<?, ?>, Object> parsedParameters) {
        this.parsedParameters = parsedParameters;
    }

    public Object getParsedParameter(String name) {
        for (ParameterCommandNode<?, ?> command : this.parsedParameters.keySet()) {
            if (command.getName().equals(name)) {
                return this.parsedParameters.get(command);
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    public <T> T getParsedParameter(String name, Class<T> type) {
        //noinspection unchecked
        return (T) this.getParsedParameter(name);
    }

    @SuppressWarnings("unused")
    public <T> T getParsedParameterOrDefault(String name, Class<T> type, T defaultValue) {
        T value = this.getParsedParameter(name, type);
        return value != null ? value : defaultValue;
    }

}
