package de.codelix.commandapi.core;

import de.codelix.commandapi.core.exception.NoRunParseException;
import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.message.CommandDesign;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.parser.Source;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.ArgumentBuilder;
import de.codelix.commandapi.core.tree.builder.Factory;
import de.codelix.commandapi.core.tree.builder.LiteralBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("unused")
public interface Command<S extends Source<M>, M, D extends CommandDesign<M>, L extends LiteralBuilder<?, ?, S, M>, A extends ArgumentBuilder<?, ?, ?, S, M>> {

    Factory<L, A, S, M> factory();

    Node<S, M> getRootNode();

    D getDesign();

    default List<List<Node<S, M>>> flatten(S source) {
        return this.getRootNode().flatten(source);
    }

    default void runSafe(List<String> input, S source) {
        try {
            this.run(input, source);
        } catch (ParseException e) {
            M message = this.getDesign().getMessages().getMessage(e);
            if (message == null) return;
            source.sendMessage(message);
        }
    }

    default void run(List<String> input, S source) throws ParseException {
        ParseContext<S, M> ctx = this.createParseContext(input, source);
        this.run(ctx);
    }

    default void run(ParseContext<S, M> ctx) throws ParseException {
        ParsedCommand<S, M> cmd = this.execute(ctx);
        if (cmd.getException() != null) {
            throw cmd.getException();
        }
        Node<S, M> lastNode = cmd.getNodes().get(cmd.getNodes().size() - 1);
        Collection<RunConsumer> runConsumers = lastNode.getRunConsumers();
        if (runConsumers.isEmpty()) {
            throw new NoRunParseException();
        }
        for (RunConsumer runConsumer : runConsumers) {
            for (Method method : runConsumer.getClass().getDeclaredMethods()) {
                this.invokeWithParameters(ctx, cmd, runConsumer, method);
            }
        }
    }

    private ParsedCommand<S, M> execute(ParseContext<S, M> ctx) {
        ParsedCommand<S, M> cmd = new ParsedCommand<>();
        this.getRootNode().parseRecursive(ctx, cmd);
        return cmd;
    }

    default CompletableFuture<List<String>> getSuggestions(List<String> input, S source) {
        ParseContext<S, M> ctx = this.createParseContext(input, source);
        return this.getSuggestions(ctx);
    }

    /**
     * Nodes contains a list with all parsed nodes. If this list is empty we just suggest from
     * the root node. If not we check if the input queue is empty. An empty queue means the command
     * has found a valid route. This also means that there is no empty space at the end as the queue
     * would contain an empty string otherwise. If the queue is empty we add the last parsed string
     * back to the queue because we want to obtain suggestions with it. If the last node was parsed
     * successful without a leading empty space we want to get suggestions from all children of the
     * node before the last node. If there is no "node before the last node" we do suggestions with
     * the root node again. Otherwise, we use the node before the last node and get suggestions from it.
     */
    default CompletableFuture<List<String>> getSuggestions(ParseContext<S, M> ctx) {
        ParsedCommand<S, M> cmd = this.execute(ctx);
        List<Node<S, M>> nodes = cmd.getNodes();
        if (nodes.isEmpty()) return this.getRootNode().getSuggestions(ctx, cmd);
        Node<S, M> lastNode = nodes.get(nodes.size() - 1);
        if (ctx.getInput().isEmpty()) {
            ctx.getInput().add(cmd.getParsed(lastNode));
            if (nodes.size() <= 1) {
                return this.getRootNode().getSuggestions(ctx, cmd);
            }
            lastNode = nodes.get(nodes.size() - 2);
        }
        List<CompletableFuture<List<String>>> futures = new ArrayList<>();
        for (Node<S, M> child : lastNode.getChildrenOptional()) {
            if (child.isVisible(ctx.getSource())) {
                futures.add(child.getSuggestions(ctx.copy(), cmd));
            }
        }
        return this.combine(futures);
    }

    default CompletableFuture<List<String>> combine(List<CompletableFuture<List<String>>> futures) {
        CompletableFuture<Void> combined = CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        CompletableFuture<List<String>> result = new CompletableFuture<>();
        combined.whenComplete((a, b) -> {
            List<String> suggestions = new ArrayList<>();
            for (CompletableFuture<List<String>> future : futures) {
                try {
                    List<String> futureSuggestions = future.get();
                    suggestions.addAll(futureSuggestions);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
            result.complete(suggestions);
        });
        return result;
    }

    default ParseContext<S, M> createParseContext(List<String> input, S source) {
        LinkedList<String> linkedInput = new LinkedList<>(input);
        return new ParseContext<>(linkedInput, source);
    }


    default void invokeWithParameters(ParseContext<S, M> ctx, ParsedCommand<S, M> cmd, Object obj, Method method) {
        java.lang.reflect.Parameter[] parameters = method.getParameters();
        Object[] params = new Object[parameters.length];
        if (params.length > 0) {
            params[0] = ctx.getSource();
        }
        if (obj instanceof RunConsumer.RC<?, ?>) {
            params[1] = cmd;
        } else {
            for (int i = 1; i < parameters.length; i++) {
                params[i] = cmd.getArgument(i-1);
            }
        }
        try {
            method.setAccessible(true);
            method.invoke(obj, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
