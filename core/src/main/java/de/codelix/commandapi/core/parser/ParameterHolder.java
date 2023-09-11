package de.codelix.commandapi.core.parser;

import de.codelix.commandapi.core.tree.ParameterTreeCommand;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public abstract class ParameterHolder {
    @Getter
    protected final Map<ParameterTreeCommand<?, ?>, Object> parsedParameters;

    public ParameterHolder() {
        this(new HashMap<>());
    }

    public ParameterHolder(Map<ParameterTreeCommand<?, ?>, Object> parsedParameters) {
        this.parsedParameters = parsedParameters;
    }

    public Object getParsedParameter(String name) {
        for (ParameterTreeCommand<?, ?> command : this.parsedParameters.keySet()) {
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
