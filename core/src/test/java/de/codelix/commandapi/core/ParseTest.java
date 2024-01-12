package de.codelix.commandapi.core;

import de.codelix.commandapi.core.exception.SyntaxException;
import de.codelix.commandapi.core.parameter.impl.WordParameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.tree.core.CoreBuilder;
import de.codelix.commandapi.core.tree.core.CoreLiteralBuilder;
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
        CoreBuilder<String> coreBuilder = new CoreBuilder<>();
        CoreLiteralBuilder<String> b = coreBuilder.literal("felix");
        b.then(coreBuilder.argument("start", new WordParameter<>())
            .then(coreBuilder.argument("end", new WordParameter<>())
                .run((source, arg0, arg1) -> {
                    System.out.println(arg0);
                    System.out.println(arg1);
                })
            )
        );

        LiteralImpl<String> literal = b.build();
        ParsedCommand<String> cmd = new ParsedCommand<>();
        LinkedList<String> input = new LinkedList<>(List.of("felix", "kanns", "awdawd"));
        ParseContext<String> ctx = new ParseContext<>(input, "Test Lol");
        literal.parseRecursive(ctx, cmd);
        for (RunConsumer<String> runConsumer : cmd.getNodes().get(cmd.getNodes().size() - 1).getRunConsumers()) {
            for (Method method : runConsumer.getClass().getDeclaredMethods()) {
                this.invokeWithParameters(cmd, runConsumer, method);
            }
        }
    }


    public void invokeWithParameters(ParsedCommand<String> cmd, Object obj, Method method) throws InvocationTargetException, IllegalAccessException {
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
        method.invoke(obj, params);
    }

}
