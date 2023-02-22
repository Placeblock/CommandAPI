package de.placeblock.commandapi.core.parser;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public abstract class ParameterHolder {
    @Getter
    protected final Map<String, Object> parsedParameters;

    public ParameterHolder() {
        this(new HashMap<>());
    }

    public ParameterHolder(Map<String, Object> parsedParameters) {
        this.parsedParameters = parsedParameters;
    }

    public Object getParsedParameter(String name) {
        return this.parsedParameters.get(name);
    }

    public <T> T getParsedParameter(String name, Class<T> type) {
        //noinspection unchecked
        return (T) this.parsedParameters.get(name);
    }

    public <T> T getParsedParameterOrDefault(String name, Class<T> type, T defaultValue) {
        T value = this.getParsedParameter(name, type);
        return value != null ? value : defaultValue;
    }

}
