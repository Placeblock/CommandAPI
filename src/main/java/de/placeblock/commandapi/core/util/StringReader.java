package de.placeblock.commandapi.core.util;

import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import io.schark.design.Texts;
import net.kyori.adventure.text.TextComponent;

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

    public int readInt() throws CommandException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw new CommandSyntaxException(Texts.negative("Falsche Eingabe. <color:primary>Ganze Zahl <color:inferior>erwartet"));
        }
        try {
            return Integer.parseInt(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw new CommandSyntaxException(Texts.negative("Falsche Eingabe. <color:primary>Kommazahl <color:inferior>erwartet, <color:negative>" + number + " <color:inferior>gefunden"));
        }
    }

    public long readLong() throws CommandException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw new CommandSyntaxException(Texts.negative("Falsche Eingabe. <color:primary>lange ganze Zahl <color:inferior>erwartet"));
        }
        try {
            return Long.parseLong(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw new CommandSyntaxException(Texts.negative("Falsche Eingabe. <color:primary>Kommazahl <color:inferior>erwartet, <color:negative>" + number + " <color:inferior>gefunden"));
        }
    }

    public double readDouble() throws CommandException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw new CommandSyntaxException(Texts.negative("Falsche Eingabe. <color:primary>Kommazahl <color:inferior>erwartet"));
        }
        try {
            return Double.parseDouble(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw new CommandSyntaxException(Texts.negative("Falsche Eingabe. <color:primary>Kommazahl <color:inferior>erwartet, <color:negative>" + number + " <color:inferior>gefunden"));
        }
    }

    public float readFloat() throws CommandException {
        final int start = cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        final String number = string.substring(start, cursor);
        if (number.isEmpty()) {
            throw new CommandSyntaxException(Texts.negative("Falsche Eingabe. <color:primary>Kommazahl <color:inferior>erwartet"));
        }
        try {
            return Float.parseFloat(number);
        } catch (final NumberFormatException ex) {
            cursor = start;
            throw new CommandSyntaxException(Texts.negative("Falsche Eingabe. <color:primary>Kommazahl <color:inferior>erwartet, <color:negative>" + number + " <color:inferior>gefunden"));
        }
    }

    public static boolean isAllowedInUnquotedString(final char c) {
        return c >= '0' && c <= '9'
            || c >= 'A' && c <= 'Z'
            || c >= 'a' && c <= 'z'
            || c == '_' || c == '-'
            || c == '.' || c == '+';
    }

    public String readUnquotedString() {
        final int start = cursor;
        while (canRead() && isAllowedInUnquotedString(peek())) {
            skip();
        }
        return string.substring(start, cursor);
    }

    public String readQuotedString() throws CommandException {
        if (!canRead()) {
            return "";
        }
        final char next = peek();
        if (!isQuotedStringStart(next)) {
            throw new CommandSyntaxException(Texts.negative("Anführungszeichen erwartet"));
        }
        skip();
        return readStringUntil(next);
    }

    public String readStringUntil(char terminator) throws CommandException {
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
                    throw new CommandSyntaxException(Texts.negative("Falsches Zeichen '"+c+"' <color:inferior>im in Anführungszeichen gesetzten Text"));
                }
            } else if (c == SYNTAX_ESCAPE) {
                escaped = true;
            } else if (c == terminator) {
                return result.toString();
            } else {
                result.append(c);
            }
        }
        throw new CommandSyntaxException(Texts.negative("Schließendes Anführungszeichen erwartet"));

    }

    public String readString() throws CommandException {
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

    public boolean readBoolean() throws CommandException {
        final int start = cursor;
        final String value = readString();
        if (value.isEmpty()) {
            throw new CommandSyntaxException(Texts.negative("Falsche Eingabe. <color:primary>true <color:inferior>oder <color:primary>false <color:inferior>erwartet"));
        }

        if (value.equals("true")) {
            return true;
        } else if (value.equals("false")) {
            return false;
        } else {
            cursor = start;
            throw new CommandSyntaxException(Texts.negative("Falsche Eingabe. <color:primary>true <color:inferior>oder <color:primary>false <color:inferior>erwartet, <color:negative>" + value + " <color:inferior>gefunden"));
        }
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
