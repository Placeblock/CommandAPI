package de.codelix.commandapi.minecraft;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public abstract class CommandAudience implements ForwardingAudience {
    protected final Audience sourceAudience;
    private final TextComponent prefix;

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return List.of(this.sourceAudience);
    }
    public void sendMessageRaw(final @NotNull Component message) {
        for (final Audience audience : this.audiences()) audience.sendMessage(message);
    }
    @Override
    public void sendMessage(final @NotNull Component message) {
        for (final Audience audience : this.audiences()) audience.sendMessage(getMessage(message));
    }
    public void sendMessageRaw(final @NotNull Component message, final ChatType.@NotNull Bound boundChatType) {
        for (final Audience audience : this.audiences()) audience.sendMessage(message, boundChatType);
    }
    @Override
    public void sendMessage(final @NotNull Component message, final ChatType.@NotNull Bound boundChatType) {
        for (final Audience audience : this.audiences()) audience.sendMessage(getMessage(message), boundChatType);
    }

    private TextComponent getMessage(Component message) {
        return this.prefix.append(message);
    }
}
