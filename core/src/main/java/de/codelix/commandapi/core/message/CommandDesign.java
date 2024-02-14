package de.codelix.commandapi.core.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommandDesign<M> {

    private final CommandMessages<M> messages;

    public void merge(CommandMessages<M> messages) {
        this.messages.addAll(messages);
    }

}
