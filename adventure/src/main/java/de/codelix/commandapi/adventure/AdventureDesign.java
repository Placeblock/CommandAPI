package de.codelix.commandapi.adventure;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.message.CommandMessages;
import de.codelix.commandapi.core.parser.Source;
import de.codelix.commandapi.core.tree.Argument;
import de.codelix.commandapi.core.tree.Literal;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.minecraft.MinecraftDesign;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.List;

public class AdventureDesign extends MinecraftDesign<Component> {
    private final TextColor primaryColor;
    private final TextColor inferiorColor;

    public AdventureDesign(CommandMessages<Component> messages) {
        this(messages, NamedTextColor.BLUE, NamedTextColor.DARK_GRAY);
    }

    public AdventureDesign(CommandMessages<Component> messages, TextColor primaryColor, TextColor inferiorColor) {
        super(messages);
        this.primaryColor = primaryColor;
        this.inferiorColor = inferiorColor;
    }

    public <S extends Source<Component>> Component getHelpHeadline(Command<S, Component, ?, ?, ?> command) {
        Node<?, Component> rootNode = command.getRootNode();
        return Component.text("---===[ ")
            .append( this.createNodeHelp(rootNode, this.primaryColor) )
            .append( Component.text(" ]===---"))
            .append( Component.newline())
            .color(this.primaryColor);
    }

    public <S extends Source<Component>> Component getNodeHelp(Node<S, Component> node) {
        if (node instanceof Literal<S, Component>) {
            return Component.text(node.getDisplayNameSafe()).color(this.inferiorColor);
        } else if (node instanceof Argument<?, S, Component>) {
            return Component.text("[")
                .append( Component.text(node.getDisplayNameSafe()) )
                .append( Component.text("]")).color(this.inferiorColor);
        }
        return null;
    }

    public <S extends Source<Component>> Component getNodeDescription(Node<S, Component> node) {
        if (node instanceof Literal<S, Component> literal) {
            List<String> names = literal.getNames();
            if (names.size() <= 1) return null;
            return Component.text("Alias: " + String.join(", ", names.subList(1, names.size()))).color(this.primaryColor);
        }
        return null;
    }

    @Override
    public <S extends Source<Component>> Component getHelpMessage(Command<S, Component, ?, ?, ?> command, List<List<Node<S, Component>>> branches) {
        Component helpMessage = Component.newline().append(this.getHelpHeadline(command));
        for (List<Node<S, Component>> branch : branches) {
            // We only want to generate the branchCommand to the first Parameter
            boolean argumentReached = false;
            StringBuilder branchCommand = new StringBuilder("/");
            Component branchMessage = Component.text("/").color(this.primaryColor);
            for (int i = 0; i < branch.size(); i++) {
                Node<S, Component> node = branch.get(i);
                if (node instanceof Argument<?,S, Component>) {
                    argumentReached = true;
                }
                TextColor color = i == 0 ? this.primaryColor : this.inferiorColor;
                Component nodeHelp = this.createNodeHelp(node, color);
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
    private <S extends Source<Component>> Component createNodeHelp(Node<S, Component> node, TextColor color) {
        Component nodeHelp = this.getNodeHelp(node).color(color);
        Component hoverText = Component.empty();
        String description = node.getDescription();
        Component extraDescription = this.getNodeDescription(node);
        if (description != null) hoverText = hoverText.append(Component.text(description).color(this.inferiorColor));
        if (description != null && extraDescription != null) hoverText = hoverText.append(Component.newline());
        if (extraDescription != null) hoverText = hoverText.append(extraDescription);
        if (description != null || extraDescription != null) {
            nodeHelp = nodeHelp.hoverEvent(HoverEvent.showText(hoverText));
        }
        return nodeHelp;
    }
}
