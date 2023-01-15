package de.placeblock.commandapi.core;

import de.placeblock.commandapi.core.tree.builder.LiteralTreeCommandBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Author: Placeblock
 */
@Getter
@RequiredArgsConstructor
public class Command<S> {

    private final LiteralTreeCommandBuilder<S> base;

    public void parse(String text) {
    }

}
