package de.codelix.commandapi.core.parameter;

import de.codelix.commandapi.core.SuggestionBuilder;
import de.codelix.commandapi.core.exception.CommandParseException;
import de.codelix.commandapi.core.exception.NotEnoughDimensionsException;
import de.codelix.commandapi.core.exception.NumberTooBigException;
import de.codelix.commandapi.core.exception.NumberTooSmallException;
import de.codelix.commandapi.core.parser.ParsedCommandBranch;
import de.codelix.commandapi.core.parser.StringReader;
import org.junit.jupiter.api.Test;

import java.util.List;

public class VecNParameterTest {

    @Test
    public void vec2ParseTest() throws CommandParseException {
        VecNParameter<String> param = new VecNParameter<>(2, 0, 10);

        StringReader reader = new StringReader("10 -1");
        ParsedCommandBranch<String> parsedCommandBranch = new ParsedCommandBranch<>(reader);
        try {
            param.parse(parsedCommandBranch, "source");
            assert false;
        } catch (NumberTooSmallException ignored) {}

        reader = new StringReader("11 -1");
        parsedCommandBranch = new ParsedCommandBranch<>(reader);
        try {
            param.parse(parsedCommandBranch, "source");
            assert false;
        } catch (NumberTooBigException ignored) {}

        reader = new StringReader("11");
        parsedCommandBranch = new ParsedCommandBranch<>(reader);
        try {
            param.parse(parsedCommandBranch, "source");
            assert false;
        } catch (NotEnoughDimensionsException ignored) {}

        reader = new StringReader("10 1");
        parsedCommandBranch = new ParsedCommandBranch<>(reader);
        double[] result = param.parse(parsedCommandBranch, "source");
        assert result[0] == 10 && result[1] == 1;
    }

    @Test
    public void vec2SuggestionsTest() {
        VecNParameter<String> param = new VecNParameter<>(2, 0, 10);

        SuggestionBuilder<String> suggestionBuilder = new SuggestionBuilder<>(null, "1.", "source");
        param.getSuggestions(suggestionBuilder);
        List<String> sugg = suggestionBuilder.getSuggestions();
        assert sugg.contains("1.0") && sugg.contains("1.9");

        suggestionBuilder = new SuggestionBuilder<>(null, "1. ", "source");
        param.getSuggestions(suggestionBuilder);
        sugg = suggestionBuilder.getSuggestions();
        assert sugg.contains(".") && sugg.contains("-");

        suggestionBuilder = new SuggestionBuilder<>(null, "1. 1", "source");
        param.getSuggestions(suggestionBuilder);
        sugg = suggestionBuilder.getSuggestions();
        assert sugg.contains("1.") && sugg.contains("10");

        suggestionBuilder = new SuggestionBuilder<>(null, "1. 1 ", "source");
        param.getSuggestions(suggestionBuilder);
        sugg = suggestionBuilder.getSuggestions();
        assert sugg.isEmpty();
    }

}
