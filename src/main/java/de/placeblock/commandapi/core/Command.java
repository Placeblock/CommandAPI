package de.placeblock.commandapi.core;

import de.placeblock.commandapi.core.tree.LiteralCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Author: Placeblock
 */
@Getter
@RequiredArgsConstructor
public class Command {

    private final LiteralCommand base;

    public void parse(String text) {

    }

}
