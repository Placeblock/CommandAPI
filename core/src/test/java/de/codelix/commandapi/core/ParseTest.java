package de.codelix.commandapi.core;

import de.codelix.commandapi.core.exception.SyntaxException;
import de.codelix.commandapi.core.parameter.impl.WordParameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.tree.builder.ArgumentBuilder;
import de.codelix.commandapi.core.tree.builder.LiteralBuilder;
import de.codelix.commandapi.core.tree.impl.LiteralImpl;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ParseTest {

    @Test
    public void testParse() throws SyntaxException, InvocationTargetException, IllegalAccessException {
        LiteralBuilder b = new LiteralBuilder("felix");
        b.then(new ArgumentBuilder<>("start", new WordParameter())
            .then(new ArgumentBuilder<>("end", new WordParameter())
                .runNative(this::test)
            )
        );

        LiteralImpl literal = b.build();
        ParsedCommand cmd = new ParsedCommand();
        LinkedList<String> input = new LinkedList<>(List.of("felix", "kanns", "awdawd"));
        ParseContext ctx = new ParseContext(input);
        literal.parseRecursive(ctx, cmd);
        for (RunConsumer runConsumer : cmd.getNodes().get(cmd.getNodes().size() - 1).getRunConsumers()) {
            for (Method method : runConsumer.getClass().getDeclaredMethods()) {
                this.invokeWithParameters(cmd, runConsumer, method);
            }
        }
    }

    private void test(Integer source, ParsedCommand cmd) {
        System.out.println(source);
    }


    public void invokeWithParameters(ParsedCommand cmd, Object obj, Method method) throws InvocationTargetException, IllegalAccessException {
        java.lang.reflect.Parameter[] parameters = method.getParameters();
        System.out.println(Arrays.toString(parameters));
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
        } catch (ClassCastException ignored) {}
    }

}
