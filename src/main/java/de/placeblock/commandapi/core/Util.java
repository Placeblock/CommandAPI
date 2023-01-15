package de.placeblock.commandapi.core;

import de.placeblock.commandapi.core.parser.ParseContext;

/**
 * Author: Placeblock
 */
public class Util {

    public static int readWord(ParseContext<?> context) {
        String text = context.getText();
        int cursor = context.getCursor();
        int i;
        for (i = cursor; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                return i;
            }
        }
        return i-cursor;
    }

}
