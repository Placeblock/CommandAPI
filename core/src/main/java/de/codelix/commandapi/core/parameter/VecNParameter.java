package de.codelix.commandapi.core.parameter;

import de.codelix.commandapi.core.SuggestionBuilder;
import de.codelix.commandapi.core.exception.CommandParseException;
import de.codelix.commandapi.core.exception.NotEnoughDimensionsException;
import de.codelix.commandapi.core.parser.ParsedCommandBranch;
import de.codelix.commandapi.core.parser.StringReader;
import lombok.Getter;

@SuppressWarnings("unused")
@Getter
public class VecNParameter<S> implements Parameter<S, double[]>{
    private final int dimensions;
    private final double min;
    private final double max;

    public static <S> VecNParameter<S> vec2(double min, double max) {
        return new VecNParameter<>(2, min, max);
    }

    public static <S> VecNParameter<S> vec3(double min, double max) {
        return new VecNParameter<>(3, min, max);
    }

    public static <S> VecNParameter<S> vecN(int dimensions, double min, double max) {
        return new VecNParameter<>(dimensions, min, max);
    }

    public VecNParameter(int dimensions, double min, double max) {
        if (dimensions < 2) {
            throw new IllegalArgumentException("Dimension of VecNParameter has a minimum of 2. For 1 dimensional use DoubleParameter");
        }
        this.dimensions = dimensions;
        this.min = min;
        this.max = max;
    }

    @Override
    public double[] parse(ParsedCommandBranch<S> command, S source) throws CommandParseException {
        StringReader reader = command.getReader();
        double[] values = new double[this.dimensions];
        for (int i = 0; i < this.dimensions; i++) {
            if (!reader.canRead()) {
                throw new NotEnoughDimensionsException(i, this.dimensions);
            }
            double value = reader.readDouble();
            values[i] = value;
            if (i < this.dimensions-1) {
                reader.skip();
            }
        }
        for (double value : values) {
            NumberParameter.checkBounds(value, this.min, this.max);
        }
        return values;
    }

    @Override
    public void getSuggestions(SuggestionBuilder<S> suggestionBuilder) {
        String partial = suggestionBuilder.getRemaining();
        String[] numbers = partial.split(" ");
        System.out.println(numbers.length < this.dimensions && partial.endsWith(" "));
        if (numbers.length == 0 || (numbers.length < this.dimensions && partial.endsWith(" "))) {
            DoubleParameter.calculateSuggestions(suggestionBuilder, "", this.min, this.max);
        } else if (numbers.length <= this.dimensions && !partial.endsWith(" ")) {
            String lastNumber = numbers[numbers.length-1];
            DoubleParameter.calculateSuggestions(suggestionBuilder, lastNumber, this.min, this.max);
        }
    }
}
