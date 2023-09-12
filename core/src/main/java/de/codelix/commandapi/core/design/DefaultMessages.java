package de.codelix.commandapi.core.design;

import de.codelix.commandapi.core.Command;

public interface DefaultMessages {

    @SuppressWarnings("unused")
    default void register() {
        register(Command.DESIGN);
    }

    void register(CommandDesign design);

}
