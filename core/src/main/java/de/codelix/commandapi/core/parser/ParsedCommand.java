package de.codelix.commandapi.core.parser;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.Argument;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@SuppressWarnings("unused")
public class ParsedCommand<S extends Source<M>, M> {
    @Getter
    private final List<Node<S, M>> nodes = new ArrayList<>();
    private final LinkedHashMap<Argument<?, S, M>, Object> arguments = new LinkedHashMap<>();
    private final LinkedHashMap<Node<S, M>, String> parsed = new LinkedHashMap<>();
    @Getter
    @Setter
    private ParseException exception;

    public void storeArgument(Argument<?, S, M> argument, Object value) {
        this.arguments.put(argument, value);
    }

    public void storeParsed(Node<S, M> node, String value) {
        this.parsed.put(node, value);
    }

    public Object getArgument(String name) {
        for (Map.Entry<Argument<?, S, M>, Object> parameterEntry : this.arguments.entrySet()) {
            if (parameterEntry.getKey().getName().equals(name)) {
                return this.arguments.get(parameterEntry.getKey());
            }
        }
        return null;
    }

    public Object getArgument(int index) {
        return this.arguments.values().toArray()[index];
    }

    public String getParsed(Node<S, M> node) {
        return this.parsed.get(node);
    }

    public void addNode(Node<S, M> node) {
        this.nodes.add(node);
    }
}
