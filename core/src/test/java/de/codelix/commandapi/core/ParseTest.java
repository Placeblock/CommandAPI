package de.codelix.commandapi.core;

import de.codelix.commandapi.core.exception.SyntaxException;
import de.codelix.commandapi.core.parameter.impl.WordParameter;
import de.codelix.commandapi.core.tree.core.CoreCommand;
import de.codelix.commandapi.core.tree.core.CoreFactory;
import de.codelix.commandapi.core.tree.core.CoreLiteralBuilder;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ParseTest {
    @Test
    public void testParse() {
        CoreFactory<String> coreBuilder = new CoreFactory<>();
        CoreLiteralBuilder<String> b = coreBuilder.literal("felix");
        b.then(coreBuilder.argument("start", new WordParameter<>())
            .then(coreBuilder.argument("end", new WordParameter<>())
                .run((String source, String start, String end) -> {
                    System.out.println(start);
                    System.out.println(end);
                })
            )
        );
        CoreCommand<String> command = new CoreCommand<>(b.build());
        command.run(List.of("felix", "kanns", "awdawd"), "Test Lol");
        command.getSuggestions(List.of("felix", "kanns", "awdawd"), "Test Lol").whenComplete((s, e) -> {
            System.out.println(s);
        });
    }

}
