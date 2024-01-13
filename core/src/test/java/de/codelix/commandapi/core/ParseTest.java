package de.codelix.commandapi.core;

import de.codelix.commandapi.core.exception.SyntaxException;
import de.codelix.commandapi.core.parameter.impl.SetParameter;
import de.codelix.commandapi.core.tree.core.CoreCommand;
import de.codelix.commandapi.core.tree.core.CoreFactory;
import de.codelix.commandapi.core.tree.core.CoreLiteralBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

public class ParseTest {
    @Test
    public void testParse() throws SyntaxException {
        CoreFactory<String> coreBuilder = new CoreFactory<>();
        CoreLiteralBuilder<String> b = coreBuilder.literal("felix");
        b.then(coreBuilder.argument("verb", new SetParameter<>(Set.of("spielt", "isst", "nervt")))
            .run((String source, String verb) -> {
                System.out.println(verb);
            })
        );
        CoreCommand<String> command = new CoreCommand<>(b.build());
        command.run(List.of("felix", "isst"), "Test Lol");

    }

}
