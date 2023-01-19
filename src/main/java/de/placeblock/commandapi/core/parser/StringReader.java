package de.placeblock.commandapi.core.parser;


import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import io.schark.design.texts.Texts;

@SuppressWarnings("unused")
public class StringReader {
    private static final char SYNTAX_ESCAPE = '\\';
    private static final char SYNTAX_DOUBLE_QUOTE = '"';
    private static final char SYNTAX_SINGLE_QUOTE = '\'';

    private final String string;
    private int cursor;

    public StringReader(final StringReader other) {
        this.string = other.string;
        this.cursor = other.cursor;
    }

    public StringReader(final String string) {
        this.string = string;
    }


    public String getString() {
        return string;
    }

    public void setCursor(final int cursor) {
        this.cursor = cursor;
    }


    public int getRemainingLength() {
        return string.length() - cursor;
    }


    public int getTotalLength() {
        return string.length();
    }


    public int getCursor() {
        return cursor;
    }


    public String getRead() {
        return string.substring(0, cursor);
    }


    public String getRemaining() {
        return string.substring(cursor);
    }


    public boolean canRead(final int length) {
        return cursor + length <= string.length();
    }


    public boolean canRead() {
        return canRead(1);
    }


    public char peek() {
        return string.charAt(cursor);
    }


    public char peek(final int offset) {
        return string.charAt(cursor + offset);
    }

    public char read() {
        return string.charAt(cursor++);
    }

    public void skip() {
        cursor++;
    }

    public static boolean isAllowedNumber(final char c) {
        return c >= '0' && c <= '9' || c == '.' || c == '-';
    }

    public static boolean isQuotedStringStart(char c) {
        return c == SYNTAX_DOUBLE_QUOTE || c == SYNTAX_SINGLE_QUOTE;
    }

    public void skipWhitespace() {
        while (canRead() && Character.isWhitespace(peek())) {
            skip();
        }
    }

    public ParsedValue<Integer> readInt() {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        ParsedValue<Integer> parsed = new ParsedValue<>(number);
        if (number.isEmpty()) {
            parsed.setSyntaxException(new CommandSyntaxException(Texts.negative("Falsche Eingabe. <color:primary>Ganze Zahl <color:inferior>erwartet")));
        }
        try {
            parsed.setValue(Integer.parseInt(number));
        } catch (final NumberFormatException ex) {
            cursor = start;
            parsed.setSyntaxException(new CommandSyntaxException(Texts.negative("Falsche Eingabe. <color:primary>Kommazahl <color:inferior>erwartet, <color:negative>" + number + " <color:inferior>gefunden")));
        }
        return parsed;
    }

    public ParsedValue<Long> readLong() {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        ParsedValue<Long> parsed = new ParsedValue<>(number);
        if (number.isEmpty()) {
            parsed.setSyntaxException(new CommandSyntaxException(Texts.negative("Falsche Eingabe. <color:primary>lange ganze Zahl <color:inferior>erwartet")));
        }
        try {
            parsed.setValue(Long.parseLong(number));
        } catch (final NumberFormatException ex) {
            cursor = start;
            parsed.setSyntaxException(new CommandSyntaxException(Texts.negative("Falsche Eingabe. <color:primary>Kommazahl <color:inferior>erwartet, <color:negative>" + number + " <color:inferior>gefunden")));
        }
        return parsed;
    }

    public ParsedValue<Double> readDouble() {
        final int start = this.cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = this.string.substring(start, this.cursor);
        ParsedValue<Double> parsed = new ParsedValue<>(number);
        if (number.isEmpty()) {
            parsed.setSyntaxException(new CommandSyntaxException(Texts.negative("Falsche Eingabe. <color:primary>Kommazahl <color:inferior>erwartet")));
        }
        try {
            parsed.setValue(Double.parseDouble(number));
        } catch (final NumberFormatException ex) {
            parsed.setSyntaxException(new CommandSyntaxException(Texts.negative("Falsche Eingabe. <color:primary>Kommazahl <color:inferior>erwartet, <color:negative>" + number + " <color:inferior>gefunden")));
        }
        return parsed;
    }

    public ParsedValue<Float> readFloat() {
        final int start = this.cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = this.string.substring(start, this.cursor);
        ParsedValue<Float> parsed = new ParsedValue<>(number);
        if (number.isEmpty()) {
            parsed.setSyntaxException(new CommandSyntaxException(Texts.negative("Falsche Eingabe. <color:primary>Kommazahl <color:inferior>erwartet")));
        }
        try {
            parsed.setValue(Float.parseFloat(number));
        } catch (final NumberFormatException ex) {
            parsed.setSyntaxException(new CommandSyntaxException(Texts.negative("Falsche Eingabe. <color:primary>Kommazahl <color:inferior>erwartet, <color:negative>" + number + " <color:inferior>gefunden")));
        }
        return parsed;
    }

    public static boolean isAllowedInUnquotedString(final char c) {
        return c >= '0' && c <= '9'
            || c >= 'A' && c <= 'Z'
            || c >= 'a' && c <= 'z'
            || c == '_' || c == '-'
            || c == '.' || c == '+';
    }

    public ParsedValue<String> readUnquotedString() {
        final int start = this.cursor;
        while (canRead() && isAllowedInUnquotedString(peek())) {
            skip();
        }
        String unquotedString = this.string.substring(start, this.cursor);
        return new ParsedValue<>(unquotedString, unquotedString, null);
    }

    public ParsedValue<String> readQuotedString() {
        ParsedValue<String> parsedError = new ParsedValue<>(null, "", new CommandSyntaxException(Texts.negative("Anführungszeichen erwartet")));
        if (!canRead()) {
            return parsedError;
        }
        final char next = peek();
        if (!isQuotedStringStart(next)) {
            return parsedError;
        }
        skip();
        return readStringUntil(next);
    }

    public ParsedValue<String> readStringUntil(char terminator) {
        final StringBuilder result = new StringBuilder();
        ParsedValue<String> parsed = new ParsedValue<>();
        boolean escaped = false;
        boolean finished = false;
        while (canRead()) {
            final char c = read();
            if (escaped) {
                if (c == terminator || c == SYNTAX_ESCAPE) {
                    result.append(c);
                    escaped = false;
                } else {
                    setCursor(getCursor() - 1);
                    parsed.setSyntaxException(new CommandSyntaxException(Texts.negative("Falsches Zeichen '"+c+"' <color:inferior>im in Anführungszeichen gesetzten Text")));
                    break;
                }
            } else if (c == SYNTAX_ESCAPE) {
                escaped = true;
            } else if (c == terminator) {
                finished = true;
                break;
            } else {
                result.append(c);
            }
        }
        if (!finished) {
            parsed.setSyntaxException(new CommandSyntaxException(Texts.negative("Schließendes Anführungszeichen erwartet")));
        } else {
            parsed.setValue(result.toString());
        }
        return parsed;
    }

    public ParsedValue<String> readString() {
        if (!canRead()) {
            return new ParsedValue<>(null, "", new CommandSyntaxException(Texts.inferior("Leerer Text <color:inferior>kann nicht gelesen werden")));
        }
        final char next = peek();
        if (isQuotedStringStart(next)) {
            skip();
            return readStringUntil(next);
        }
        return readUnquotedString();
    }

    public ParsedValue<Boolean> readBoolean() {
        final ParsedValue<String> value = readString();
        ParsedValue<Boolean> parsed = new ParsedValue<>(null, value.getValue(),
            new CommandSyntaxException(Texts.negative("Falsche Eingabe. <color:primary>true <color:inferior>oder <color:primary>false <color:inferior>erwartet")));
        if (value.getValue() == null) {
            parsed.setSyntaxException(value.getSyntaxException());
            return parsed;
        }
        if (value.getValue().equals("true")) {
            parsed.setValue(true);
        } else if (value.getValue().equals("false")) {
            parsed.setValue(false);
        } else {
            return parsed;
        }
        parsed.setSyntaxException(null);
        return parsed;
    }

    public void expect(final char c) throws CommandException {
        if (!canRead() || peek() != c) {
            throw new CommandSyntaxException(Texts.negative("Falsche Eingabe. <color:primary>Zeichen '"+c+"' <color:inferior>erwartet"));
        }
        skip();
    }

    public String debugString() {
        return "'" + this.getString().substring(0, this.getCursor()) + "|" + this.getString().substring(this.getCursor()) + "'";
    }
}
