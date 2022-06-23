package de.placeblock.commandapi.suggestion;

import de.placeblock.commandapi.util.Message;
import de.placeblock.commandapi.util.StringRange;

import java.util.Objects;

public class IntegerSuggestion extends Suggestion {
    private int value;

    public IntegerSuggestion(final StringRange range, final int value) {
        this(range, value, null);
    }

    public IntegerSuggestion(final StringRange range, final int value, final Message tooltip) {
        super(range, Integer.toString(value), tooltip);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final IntegerSuggestion that)) {
            return false;
        }
        return value == that.value && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }

    @Override
    public String toString() {
        return "IntegerSuggestion{" +
            "value=" + value +
            ", range=" + getRange() +
            ", text='" + getText() + '\'' +
            ", tooltip='" + getTooltip() + '\'' +
            '}';
    }

    @Override
    public int compareTo(final Suggestion o) {
        if (o instanceof IntegerSuggestion) {
            return Integer.compare(value, ((IntegerSuggestion) o).value);
        }
        return super.compareTo(o);
    }

    @Override
    public int compareToIgnoreCase(final Suggestion b) {
        return compareTo(b);
    }
}
