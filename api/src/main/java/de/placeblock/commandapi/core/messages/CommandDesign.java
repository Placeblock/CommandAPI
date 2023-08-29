package de.placeblock.commandapi.core.messages;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.exception.CommandParseException;
import de.placeblock.commandapi.core.tree.LiteralTreeCommand;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;
import de.placeblock.commandapi.core.tree.TreeCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class CommandDesign {
    private final Map<Class<? extends CommandParseException>, Function<CommandParseException, TextComponent>> messages = new HashMap<>();

    public <T extends CommandParseException> void register(Class<T> clazz, Function<T, TextComponent> message) {
        this.messages.put(clazz, ex -> message.apply(clazz.cast(message)));
    }

    public <T extends CommandParseException>  TextComponent getMessage(T exception) {
        return this.messages.get(exception.getClass()).apply(exception);
    }

    public abstract TextComponent getPrefix(Command<?> commandName);

    public abstract TextComponent getHelpHeadline(Command<?> commandName);

    public abstract TextComponent getHelpLiteralTreeCommand(LiteralTreeCommand<?> literal);

    public abstract TextComponent getHelpParameterTreeCommand(ParameterTreeCommand<?, ?> parameter);

    public abstract TextComponent getHelpLiteralTreeCommandDescription(LiteralTreeCommand<?> literal);

    public abstract TextComponent getHelpParameterTreeCommandDescription(ParameterTreeCommand<?, ?> parameter);

    public <S> TextComponent generateHelpMessage(Command<S> command, S source) {
        List<List<TreeCommand<S>>> branches = command.getBase().getBranches(source);
        TextComponent helpMessage = Component.newline()
            .append(getHelpHeadline(command));
        for (List<TreeCommand<S>> branch : branches) {
            // We only want to generate the branchCommand to the first Parameter
            boolean parameterReached = false;
            StringBuilder branchCommand = new StringBuilder("/");
            TextComponent branchMessage = Component.text("/").color(NamedTextColor.BLUE);
            for (int i = 0; i < branch.size(); i++) {
                TreeCommand<S> treeCommand = branch.get(i);
                if (treeCommand instanceof ParameterTreeCommand<?,?>) {
                    parameterReached = true;
                }
                TextColor color = i == 0 ? NamedTextColor.BLUE : TextColor.fromHexString("#4d4d4d");
                TextComponent treeCommandMessage = treeCommand.getHelpComponent().color(color);
                TextComponent hoverText = Component.empty();
                TextComponent description = treeCommand.getDescription();
                TextComponent extraDescription = treeCommand.getHelpExtraDescription();
                if (description != null) hoverText = hoverText.append(description);
                if (description != null && extraDescription != null) hoverText = hoverText.append(Component.newline());
                if (extraDescription != null) hoverText = hoverText.append(extraDescription);
                if (description != null || extraDescription != null) {
                    treeCommandMessage = treeCommandMessage.hoverEvent(HoverEvent.showText(hoverText));
                }
                branchMessage = branchMessage.append(treeCommandMessage).append(Component.space());
                if (!parameterReached) {
                    branchCommand.append(treeCommand.getName()).append(" ");
                }
            }
            branchMessage = branchMessage.clickEvent(ClickEvent.suggestCommand(branchCommand.toString()));
            helpMessage = helpMessage.append(branchMessage.append(Component.newline()));
        }
        return helpMessage;
    }
}
