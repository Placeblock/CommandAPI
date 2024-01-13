package de.codelix.commandapi.core;

import de.codelix.commandapi.core.exception.SyntaxException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.ArgumentBuilder;
import de.codelix.commandapi.core.tree.builder.Factory;
import de.codelix.commandapi.core.tree.builder.LiteralBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface Command<L extends LiteralBuilder<?, ?, S>, A extends ArgumentBuilder<?, ?, ?, S>, S> {

    Factory<L, A, S> factory();

    Node<S> getRootNode();

    boolean hasPermission(S source, String permission);

    default void run(List<String> input, S source) throws SyntaxException {
        ParseContext<S> ctx = this.createParseContext(input, source);
        this.run(ctx);
    }

    default void run(ParseContext<S> ctx) throws SyntaxException {
        ParsedCommand<S> cmd = this.execute(ctx);
        if (cmd.getException() != null) {
            throw cmd.getException();
        }
        for (RunConsumer<S> runConsumer : cmd.getNodes().get(cmd.getNodes().size() - 1).getRunConsumers()) {
            for (Method method : runConsumer.getClass().getDeclaredMethods()) {
                this.invokeWithParameters(cmd, runConsumer, method);
            }
        }
    }

    private ParsedCommand<S> execute(ParseContext<S> ctx) {
        ParsedCommand<S> cmd = new ParsedCommand<>();
        this.getRootNode().parseRecursive(ctx, cmd);
        return cmd;
    }

    default CompletableFuture<List<String>> getSuggestions(List<String> input, S source) {
        ParseContext<S> ctx = this.createParseContext(input, source);
        return this.getSuggestions(ctx);
    }

    default CompletableFuture<List<String>> getSuggestions(ParseContext<S> ctx) {
        ParsedCommand<S> cmd = this.execute(ctx);
        if (ctx.getInput().isEmpty()) return CompletableFuture.completedFuture(List.of());
        List<Node<S>> nodes = cmd.getNodes();
        if (nodes.isEmpty()) return this.getRootNode().getSuggestions(ctx, cmd);
        Node<S> lastNode = nodes.get(nodes.size() - 1);
        List<CompletableFuture<List<String>>> futures = new ArrayList<>();
        for (Node<S> child : lastNode.getChildrenOptional()) {
            futures.add(child.getSuggestions(ctx.copy(), cmd));
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

    default ParseContext<S> createParseContext(List<String> input, S source) {
        LinkedList<String> linkedInput = new LinkedList<>(input);
        return new ParseContext<>(linkedInput, source, this::hasPermission);
    }


    default void invokeWithParameters(ParsedCommand<S> cmd, Object obj, Method method) {
        java.lang.reflect.Parameter[] parameters = method.getParameters();
        Object[] params = new Object[parameters.length];
        if (params.length > 0) {
            params[0] = "Placeblock";
        }
        if (obj instanceof RunConsumer.RC<?>) {
            params[1] = cmd;
        } else {
            for (int i = 1; i < parameters.length; i++) {
                params[i] = cmd.getArgument(i-1);
            }
        }
        try {
            method.invoke(obj, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
