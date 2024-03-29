package de.codelix.commandapi.adventure;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.message.CommandMessages;
import de.codelix.commandapi.core.tree.Argument;
import de.codelix.commandapi.core.tree.Literal;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.minecraft.MinecraftDesign;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.List;

public class AdventureDesign<S extends AdventureSource<?, ?>> extends MinecraftDesign<S, TextComponent> {
    private final TextColor primaryColor;
    private final TextColor inferiorColor;

    public AdventureDesign(CommandMessages<TextComponent> messages) {
        this(messages, NamedTextColor.BLUE, NamedTextColor.DARK_GRAY);
    }

    public AdventureDesign(CommandMessages<TextComponent> messages, TextColor primaryColor, TextColor inferiorColor) {
        super(messages);
        this.primaryColor = primaryColor;
        this.inferiorColor = inferiorColor;
    }

    public TextComponent getHelpHeadline(Command<S, TextComponent, ?, ?, ?> command) {
        Node<S, TextComponent> rootNode = command.getRootNode();
        return Component.text("---===[ ")
            .append( this.createNodeHelp(rootNode, this.primaryColor) )
            .append( Component.text(" ]===---"))
            .append( Component.newline())
            .color(this.primaryColor);
    }

    public TextComponent getNodeHelp(Node<S, TextComponent> node) {
        if (node instanceof Literal<S, TextComponent>) {
            return Component.text(node.getDisplayNameSafe()).color(this.inferiorColor);
        } else if (node instanceof Argument<?,?, TextComponent>) {
            return Component.text("[")
                .append( Component.text(node.getDisplayNameSafe()) )
                .append( Component.text("]")).color(this.inferiorColor);
        }
        return null;
    }

    public TextComponent getNodeDescription(Node<?, TextComponent> node) {
        if (node instanceof Literal<?, TextComponent> literal) {
            List<String> names = literal.getNames();
            if (names.size() <= 1) return null;
            return Component.text("Alias: " + String.join(", ", names.subList(1, names.size()))).color(this.primaryColor);
        }
        return null;
    }

    @Override
    public TextComponent getHelpMessage(Command<S, TextComponent, ?, ?, ?> command, S source) {
        List<List<Node<S, TextComponent>>> branches = command.flatten(source);
        TextComponent helpMessage = Component.newline().append(this.getHelpHeadline(command));
        for (List<Node<S, TextComponent>> branch : branches) {
            // We only want to generate the branchCommand to the first Parameter
            boolean argumentReached = false;
            StringBuilder branchCommand = new StringBuilder("/");
            TextComponent branchMessage = Component.text("/").color(this.primaryColor);
            for (int i = 0; i < branch.size(); i++) {
                Node<S, TextComponent> node = branch.get(i);
                if (node instanceof Argument<?,?, TextComponent>) {
                    argumentReached = true;
                }
                TextColor color = i == 0 ? this.primaryColor : this.inferiorColor;
                TextComponent nodeHelp = this.createNodeHelp(node, color);
                branchMessage = branchMessage.append(nodeHelp).append(Component.space());
                if (!argumentReached) {
                    branchCommand.append(node.getDisplayNameSafe()).append(" ");
                }
            }
            branchMessage = branchMessage.clickEvent(ClickEvent.suggestCommand(branchCommand.toString()));
            helpMessage = helpMessage.append(branchMessage.append(Component.newline()));
        }
        return helpMessage;
    }

    @NonNull
    private TextComponent createNodeHelp(Node<S, TextComponent> node, TextColor color) {
        TextComponent nodeHelp = this.getNodeHelp(node).color(color);
        TextComponent hoverText = Component.empty();
        String description = node.getDescription();
        TextComponent extraDescription = this.getNodeDescription(node);
        if (description != null) hoverText = hoverText.append(Component.text(description).color(this.inferiorColor));
        if (description != null && extraDescription != null) hoverText = hoverText.append(Component.newline());
        if (extraDescription != null) hoverText = hoverText.append(extraDescription);
        if (description != null || extraDescription != null) {
            nodeHelp = nodeHelp.hoverEvent(HoverEvent.showText(hoverText));
        }
        return nodeHelp;
    }
}
