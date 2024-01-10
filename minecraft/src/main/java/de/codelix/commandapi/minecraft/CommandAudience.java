package de.codelix.commandapi.minecraft;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class CommandAudience implements Audience {
    protected final Audience sourceAudience;
    private final TextComponent prefix;
    public void sendMessageRaw(final @NotNull Component message) {
        this.sourceAudience.sendMessage(message);
    }
    @Override
    public void sendMessage(final @NotNull Component message) {
        this.sourceAudience.sendMessage(getMessage(message));
    }
    public void sendMessageRaw(final @NotNull Component message, final ChatType.@NotNull Bound boundChatType) {
        this.sourceAudience.sendMessage(message, boundChatType);
    }
    @Override
    public void sendMessage(final @NotNull Component message, final ChatType.@NotNull Bound boundChatType) {
        this.sourceAudience.sendMessage(getMessage(message), boundChatType);
    }

    private TextComponent getMessage(Component message) {
        return this.prefix.append(message);
    }
}
