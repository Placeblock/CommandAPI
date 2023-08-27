package de.placeblock.commandapi.core.parser;


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

    public boolean canReadWord() {
        return this.canRead(2) && !this.getRemaining().isBlank();
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

    public int readInt() throws CommandSyntaxException {
        final String number = this.readUnquotedString();
        if (number.isEmpty()) {
            throw new CommandSyntaxException(Texts.inferior("Ganze Zahl erwartet"));
        }
        try {
            return Integer.parseInt(number);
        } catch (final NumberFormatException ex) {
            throw new CommandSyntaxException(Texts.primary(number + " <color:inferior>ist <color:negative>keine Ganze Zahl"));
        }
    }

    public long readLong() throws CommandSyntaxException {
        final String number = this.readUnquotedString();
        if (number.isEmpty()) {
            throw new CommandSyntaxException(Texts.inferior("Ganze Zahl erwartet"));
        }
        try {
            return Long.parseLong(number);
        } catch (final NumberFormatException ex) {
            throw new CommandSyntaxException(Texts.primary(number + " <color:inferior>ist <color:negative>keine Ganze Zahl"));
        }
    }

    public double readDouble() throws CommandSyntaxException {
        final String number = this.readUnquotedString();
        if (number.isEmpty()) {
            throw new CommandSyntaxException(Texts.inferior("Komma Zahl erwartet"));
        }
        try {
            return Double.parseDouble(number);
        } catch (final NumberFormatException ex) {
            throw new CommandSyntaxException(Texts.primary(number + " <color:inferior>ist <color:negative>keine Komma Zahl"));
        }
    }

    public float readFloat() throws CommandSyntaxException {
        final String number = this.readUnquotedString();
        if (number.isEmpty()) {
            throw new CommandSyntaxException(Texts.inferior("Komma Zahl erwartet"));
        }
        try {
            return Float.parseFloat(number);
        } catch (final NumberFormatException ex) {
            throw new CommandSyntaxException(Texts.primary(number + " <color:inferior>ist <color:negative>keine Komma Zahl"));
        }
    }

    public String readUnquotedString() {
        final int start = cursor;
        while (canRead() && peek() != ' ') {
            skip();
        }
        return string.substring(start, cursor);
    }

    public String readQuotedString() throws CommandSyntaxException {
        if (!canRead()) {
            return "";
        }
        final char next = peek();
        if (!isQuotedStringStart(next)) {
            throw new CommandSyntaxException(Texts.inferior("Text in Anführungszeichen erwartet"));
        }
        skip();
        return readStringUntil(next);
    }

    public String readStringUntil(char terminator) throws CommandSyntaxException {
        final StringBuilder result = new StringBuilder();
        boolean escaped = false;
        while (canRead()) {
            final char c = read();
            if (escaped) {
                if (c == terminator || c == SYNTAX_ESCAPE) {
                    result.append(c);
                    escaped = false;
                } else {
                    setCursor(getCursor() - 1);
                    throw new CommandSyntaxException(Texts.primary(c + " <color:negative>darf nicht Escaped werden"));
                }
            } else if (c == SYNTAX_ESCAPE) {
                escaped = true;
            } else if (c == terminator) {
                return result.toString();
            } else {
                result.append(c);
            }
        }

        throw new CommandSyntaxException(Texts.inferior("End-Anführungszeichen erwartet"));
    }

    public String readString() throws CommandSyntaxException {
        if (!canRead()) {
            return "";
        }
        final char next = peek();
        if (isQuotedStringStart(next)) {
            skip();
            return readStringUntil(next);
        }
        return readUnquotedString();
    }

    public boolean readBoolean() throws CommandSyntaxException {
        final String value = readUnquotedString();
        if (value.isEmpty()) {
            throw new CommandSyntaxException(Texts.inferior("true oder false erwartet"));
        }
        if (value.equals("true")) {
            return true;
        } else if (value.equals("false")) {
            return false;
        } else {
            throw new CommandSyntaxException(Texts.primary(value + " <color:primary>ist <color:negative>kein Wahrheitswert"));
        }
    }

    public void expect(final char c) throws CommandSyntaxException {
        char peek = peek();
        if (!canRead() || peek != c) {
            throw new CommandSyntaxException(Texts.primary(c + " <color:inferior>erwartet <color:negative>" + c + " <color:inferior>bekommen"));
        }
        skip();
    }

    public String debugString() {
        return "'" + this.string.substring(0, this.cursor) + "|" + this.string.substring(this.cursor) + "'";
    }
}
