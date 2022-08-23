package de.placeblock.commandapi.bridge;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommandSource<P, C> {
    private final P player;
    private final C sender;
}
