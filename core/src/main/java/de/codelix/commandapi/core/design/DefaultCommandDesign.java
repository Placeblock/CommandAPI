package de.codelix.commandapi.core.design;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.tree.LiteralCommandNode;
import de.codelix.commandapi.core.tree.ParameterCommandNode;
import de.codelix.commandapi.core.tree.CommandNode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.List;

public class DefaultCommandDesign extends CommandDesign {
    private final TextColor primaryColor;
    private final TextColor inferiorColor;

    public DefaultCommandDesign() {
        this(NamedTextColor.BLUE, NamedTextColor.DARK_GRAY);
    }

    public DefaultCommandDesign(TextColor primaryColor, TextColor inferiorColor) {
        this.primaryColor = primaryColor;
        this.inferiorColor = inferiorColor;
    }

    @Override
    public TextComponent getPrefix(Command<?> command) {
        return command.getBase().getDisplayName().append(Component.text(" » ")).color(this.primaryColor);
    }

    public TextComponent getHelpHeadline(Command<?> command) {
        return Component.text("---===[ ")
            .append( command.getBase().getDisplayName() )
            .append( Component.text(" ]===---"))
            .append( Component.newline())
            .color(this.primaryColor);
    }

    public TextComponent getHelpTreeCommand(CommandNode<?> commandNode) {
        if (commandNode instanceof LiteralCommandNode<?>) {
            return Component.text(commandNode.getName()).color(this.inferiorColor);
        } else if (commandNode instanceof ParameterCommandNode<?, ?>) {
            return Component.text("[")
                .append( commandNode.getDisplayName())
                .append( Component.text("]")).color(this.inferiorColor);
        }
        return null;
    }

    public TextComponent getHelpTreeCommandDescription(CommandNode<?> commandNode) {
        if (commandNode instanceof LiteralCommandNode<?> literalTreeCommand) {
            if (literalTreeCommand.getAliases().size() == 0) return null;
            return Component.text("Alias: " + String.join(", ", literalTreeCommand.getAliases())).color(this.primaryColor);
        }
        return null;
    }

    public <S> TextComponent generateHelpMessage(Command<S> command, S source) {
        List<List<CommandNode<S>>> branches = command.getBase().getBranches(source);
        TextComponent helpMessage = Component.newline()
            .append(this.getHelpHeadline(command));
        for (List<CommandNode<S>> branch : branches) {
            // We only want to generate the branchCommand to the first Parameter
            boolean parameterReached = false;
            StringBuilder branchCommand = new StringBuilder("/");
            TextComponent branchMessage = Component.text("/").color(this.primaryColor);
            for (int i = 0; i < branch.size(); i++) {
                CommandNode<S> commandNode = branch.get(i);
                if (commandNode instanceof ParameterCommandNode<?,?>) {
                    parameterReached = true;
                }
                TextColor color = i == 0 ? this.primaryColor : this.inferiorColor;
                TextComponent treeCommandMessage = this.getHelpTreeCommand(commandNode).color(color);
                TextComponent hoverText = Component.empty();
                TextComponent description = commandNode.getDescription();
                TextComponent extraDescription = this.getHelpTreeCommandDescription(commandNode);
                if (description != null) hoverText = hoverText.append(description);
                if (description != null && extraDescription != null) hoverText = hoverText.append(Component.newline());
                if (extraDescription != null) hoverText = hoverText.append(extraDescription);
                if (description != null || extraDescription != null) {
                    treeCommandMessage = treeCommandMessage.hoverEvent(HoverEvent.showText(hoverText));
                }
                branchMessage = branchMessage.append(treeCommandMessage).append(Component.space());
                if (!parameterReached) {
                    branchCommand.append(commandNode.getName()).append(" ");
                }
            }
            branchMessage = branchMessage.clickEvent(ClickEvent.suggestCommand(branchCommand.toString()));
            helpMessage = helpMessage.append(branchMessage.append(Component.newline()));
        }
        return helpMessage;
    }
}
