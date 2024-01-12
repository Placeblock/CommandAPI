package de.codelix.commandapi.core.parser;

import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.Argument;
import lombok.Getter;

import java.util.*;

public class ParsedCommand {
    @Getter
    private final List<Node> nodes = new ArrayList<>();
    private final LinkedHashMap<Argument<?>, Object> arguments = new LinkedHashMap<>();

    public void storeArgument(Argument<?> argument, Object value) {
        this.arguments.put(argument, value);
    }

    public Object getArgument(String name) {
        for (Map.Entry<Argument<?>, Object> parameterEntry : this.arguments.entrySet()) {
            if (parameterEntry.getKey().getName().equals(name)) {
                return this.arguments.get(parameterEntry.getKey());
            }
        }
        return null;
    }

    public Object getArgument(int index) {
        return this.arguments.values().toArray()[index];
    }

    public void addNode(Node node) {
        this.nodes.add(node);
    }
}
