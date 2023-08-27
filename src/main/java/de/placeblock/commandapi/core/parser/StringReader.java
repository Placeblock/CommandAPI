package de.placeblock.commandapi.core.parser;


import de.placeblock.commandapi.core.exception.*;
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

    public int readInt() throws CommandParseException {
        final String number = this.readUnquotedString();
        if (number.isEmpty()) {
            throw new IntegerRequiredException();
        }
        try {
            return Integer.parseInt(number);
        } catch (final NumberFormatException ex) {
            throw new InvalidIntegerException(number);
        }
    }

    public long readLong() throws CommandParseException {
        final String number = this.readUnquotedString();
        if (number.isEmpty()) {
            throw new IntegerRequiredException();
        }
        try {
            return Long.parseLong(number);
        } catch (final NumberFormatException ex) {
            throw new InvalidIntegerException(number);
        }
    }

    public double readDouble() throws CommandParseException {
        final String number = this.readUnquotedString();
        if (number.isEmpty()) {
            throw new DecimalRequiredException();
        }
        try {
            return Double.parseDouble(number);
        } catch (final NumberFormatException ex) {
            throw new InvalidDecimalException(number);
        }
    }

    public float readFloat() throws CommandParseException {
        final String number = this.readUnquotedString();
        if (number.isEmpty()) {
            throw new DecimalRequiredException();
        }
        try {
            return Float.parseFloat(number);
        } catch (final NumberFormatException ex) {
            throw new InvalidDecimalException(number);
        }
    }

    public String readUnquotedString() {
        final int start = cursor;
        while (canRead() && peek() != ' ') {
            skip();
        }
        return string.substring(start, cursor);
    }

    public String readQuotedString() throws CommandParseException {
        if (!canRead()) {
            return "";
        }
        final char next = peek();
        if (!isQuotedStringStart(next)) {
            throw new QuotedStringRequiredException();
        }
        skip();
        return readStringUntil(next);
    }

    public String readStringUntil(char terminator) throws CommandParseException {
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
                    throw new InvalidEscapeCharException(c);
                }
            } else if (c == SYNTAX_ESCAPE) {
                escaped = true;
            } else if (c == terminator) {
                return result.toString();
            } else {
                result.append(c);
            }
        }

        throw new NoEndQuoteException();
    }

    public String readString() throws CommandParseException {
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

    public boolean readBoolean() throws CommandParseException {
        final String value = readUnquotedString();
        if (value.isEmpty()) {
            throw new BooleanRequiredException();
        }
        if (value.equals("true")) {
            return true;
        } else if (value.equals("false")) {
            return false;
        } else {
            throw new InvalidBooleanException(value);
        }
    }

    public String debugString() {
        return "'" + this.string.substring(0, this.cursor) + "|" + this.string.substring(this.cursor) + "'";
    }
}
