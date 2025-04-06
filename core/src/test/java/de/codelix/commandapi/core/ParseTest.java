package de.codelix.commandapi.core;

import de.codelix.commandapi.core.parameter.impl.GreedyParameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.tree.builder.impl.DefaultArgumentBuilder;
import de.codelix.commandapi.core.tree.builder.impl.DefaultLiteralBuilder;
import de.codelix.commandapi.core.tree.impl.LiteralImpl;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

public class ParseTest {

    @Test
    public void testOnlyGreedy() {
        LiteralImpl<TestSource, String> literal = new DefaultLiteralBuilder<TestSource, String>("test")
            .then(new DefaultArgumentBuilder<String, TestSource, String>("name", new GreedyParameter<>())
                .run((TestSource source, String name) ->
                    System.out.println(name))
            ).build();
        LinkedList<String> linkedInput = new LinkedList<>();
        linkedInput.add("test");
        linkedInput.add("xD2");
        ParseContext<TestSource, String> ctx = new ParseContext<>(linkedInput, new TestSource());
        ParsedCommand<TestSource, String> cmd = new ParsedCommand<>();
        literal.parseRecursive(ctx, cmd);
        assert cmd.getException() == null;
    }

}
