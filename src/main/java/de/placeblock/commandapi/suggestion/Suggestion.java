package de.placeblock.commandapi.suggestion;

import de.placeblock.commandapi.util.Message;
import de.placeblock.commandapi.util.StringRange;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class Suggestion implements Comparable<Suggestion> {
    private final StringRange range;
    private final String text;
    private final Message tooltip;

    public Suggestion(final StringRange range, final String text) {
        this(range, text, null);
    }

    public String apply(final String input) {
        if (range.getStart() == 0 && range.getEnd() == input.length()) {
            return text;
        }
        final StringBuilder result = new StringBuilder();
        if (range.getStart() > 0) {
            result.append(input, 0, range.getStart());
        }
        result.append(text);
        if (range.getEnd() < input.length()) {
            result.append(input.substring(range.getEnd()));
        }
        return result.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final Suggestion that)) {
            return false;
        }
        return Objects.equals(range, that.range) && Objects.equals(text, that.text) && Objects.equals(tooltip, that.tooltip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(range, text, tooltip);
    }

    @Override
    public String toString() {
        return "Suggestion{" +
            "range=" + range +
            ", text='" + text + '\'' +
            ", tooltip='" + tooltip + '\'' +
            '}';
    }

    @Override
    public int compareTo(final Suggestion o) {
        return text.compareTo(o.text);
    }

    public int compareToIgnoreCase(final Suggestion b) {
        return text.compareToIgnoreCase(b.text);
    }

    public Suggestion expand(final String command, final StringRange range) {
        if (range.equals(this.range)) {
            return this;
        }
        final StringBuilder result = new StringBuilder();
        if (range.getStart() < this.range.getStart()) {
            result.append(command, range.getStart(), this.range.getStart());
        }
        result.append(text);
        if (range.getEnd() > this.range.getEnd()) {
            result.append(command, this.range.getEnd(), range.getEnd());
        }
        return new Suggestion(range, result.toString(), tooltip);
    }
}
