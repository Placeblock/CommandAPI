package de.codelix.commandapi.core.parser;

import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.Attribute;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParsedCommand {
    @Getter
    private final List<Node> nodes = new ArrayList<>();
    private final Map<Attribute<?>, Object> attributes = new HashMap<>();

    public void storeParam(Attribute<?> attribute, Object value) {
        this.attributes.put(attribute, value);
    }

    public Object getParameter(String name) {
        for (Map.Entry<Attribute<?>, Object> parameterEntry : this.attributes.entrySet()) {
            if (parameterEntry.getKey().getName().equals(name)) {
                return this.attributes.get(parameterEntry.getKey());
            }
        }
        return null;
    }

    public void addNode(Node node) {
        this.nodes.add(node);
    }
}
