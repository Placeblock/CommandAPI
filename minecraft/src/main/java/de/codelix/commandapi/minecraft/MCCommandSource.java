package de.codelix.commandapi.minecraft;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.TextComponent;

/**
 * Author: Placeblock
 */
public interface MCCommandSource {

    void sendMessage(TextComponent message);

}
