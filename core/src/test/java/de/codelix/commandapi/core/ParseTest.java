package de.codelix.commandapi.core;

import de.codelix.commandapi.core.exception.SyntaxException;
import de.codelix.commandapi.core.parameter.impl.WordParameter;
import de.codelix.commandapi.core.parser.Param;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.run.RunConsumer;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.AttributeBuilder;
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
        b.then(new LiteralBuilder("kann", "kanns")
            .then(new LiteralBuilder("nicht")
                .optional()
                .then(new LiteralBuilder("essen")
                )
            )
            .then(new LiteralBuilder("nichta")
                .then(new LiteralBuilder("essen2")
                )
            )
            .then(new AttributeBuilder<>("name", new WordParameter())
                .run((@Param("name") String playerName) -> {
                    System.out.println(playerName + " xD xD");
                })
            )
        );
        LiteralImpl literal = b.build();
        ParsedCommand cmd = new ParsedCommand();
        LinkedList<String> input = new LinkedList<>(List.of("felix", "kanns", "awdawd"));
        ParseContext ctx = new ParseContext(input);
        literal.parseRecursive(ctx, cmd);
        for (RunConsumer runConsumer : cmd.getNodes().get(cmd.getNodes().size() - 1).getRunConsumers()) {
            Method lambdaMethod = runConsumer.method();
            this.invokeWithParameters(cmd, runConsumer, lambdaMethod);
        }
        System.out.println(cmd.getParameter("name"));
    }



    public void invokeWithParameters(ParsedCommand cmd, Object obj, Method method) throws InvocationTargetException, IllegalAccessException {
        java.lang.reflect.Parameter[] parameters = method.getParameters();
        Object[] params = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            java.lang.reflect.Parameter parameter = parameters[i];
            Param annotation = parameter.getAnnotation(Param.class);
            if (annotation != null) {
                String name = annotation.value();
                params[i] = cmd.getParameter(name);
            } else {
                params[i] = null;
            }
        }
        method.invoke(obj, params);
    }

}
