package de.placeblock.commandapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
@SuppressWarnings("unused")
public abstract class CommandArgument<P> {

    private final String label;
    private final String description;

    public CommandArgument(String label) {
        this.label = label;
        this.description = "";
    }

    public abstract Set<String> getExtraTabCompletions(P player);

}
