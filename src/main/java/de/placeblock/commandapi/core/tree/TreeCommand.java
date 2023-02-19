package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.parser.ParseContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Author: Placeblock
 */
@Getter
@RequiredArgsConstructor
public abstract class TreeCommand<S> {

    private final Command<S> command;
    private final String name;
    private final List<TreeCommand<S>> children;
    private final TextComponent description;
    private final String permission;
    private final Consumer<ParseContext<S>> run;

    abstract boolean parse(ParseContext<S> context);


    public ParseResult parseRecursive(ParseContext<S> context, boolean suggestion) {
        ArrayList<TreeCommand<S>> parsedCommands = new ArrayList<>();
        ParseResult parseResult = this.parseRecursive(context, parsedCommands, suggestion);
        context.setParsedCommands(parsedCommands);
        return parseResult;
    }

    private ParseResult parseRecursive(ParseContext<S> context, List<TreeCommand<S>> parsedCommands, boolean suggestion) {
        Command.LOGGER.info("Parsing Command: " + this.name);
        Command.LOGGER.info("Current Reader: " + context.getReader().debugString());
        // Check Permissions
        if (this.hasNoPermission(context.getSource())) {
            context.setNoPermission(true);
            Command.LOGGER.info("The player has no Permission to execute this commmand. Skipping.");
            return ParseResult.NO_PERMISSION;
        }
        context.setNoPermission(false);

        // Parse the current Command
        if (!this.parse(context)) {
            Command.LOGGER.info("Couldn't parse Command.");
            return ParseResult.PARSE_ERROR;
        }
        Command.LOGGER.info("Parsed Command: " + this.name);
        Command.LOGGER.info("Current Reader: " + context.getReader().debugString());
        parsedCommands.add(this);

        // Only move forward if we haven't reached the end already
        if (!context.getReader().canRead(2)) {
            Command.LOGGER.info("Reader has no more text to read.");
            return ParseResult.SUCCESS;
        }

        int oldcursor = context.getReader().getCursor();
        boolean childParsed = false;
        // Parse Children
        for (TreeCommand<S> child : this.children) {
            Command.LOGGER.info("Parsing Child: " + child.getName());

            // Skip white space
            context.getReader().skip();

            List<TreeCommand<S>> childParsedCommands = new ArrayList<>();
            ParseResult childParseResult = child.parseRecursive(context, childParsedCommands, suggestion);
            childParsed = true;
            Command.LOGGER.info("Parse Result Of Child " + child.getName() + ": " + childParseResult);
            if (suggestion ? childParseResult.isSuggestionSuccess() : childParseResult.isExecuteSuccess()) {
                // We append the parsed child commands only if they were successful
                parsedCommands.addAll(childParsedCommands);
                return ParseResult.SUCCESS;
            } else {
                context.getReader().setCursor(oldcursor);
            }
        }
        if (childParsed) {
            return ParseResult.CHILD_PARSE_ERROR;
        }
        return ParseResult.NO_CHILDREN;
    }

    @Getter
    @RequiredArgsConstructor
    private enum ParseResult {
        SUCCESS(true, true),
        NO_PERMISSION(false, false),
        PARSE_ERROR(false, false),
        NO_CHILDREN(false, false),
        CHILD_PARSE_ERROR(false, true);

        private final boolean executeSuccess;
        private final boolean suggestionSuccess;
    }

    public List<List<TreeCommand<S>>> getBranches(S source) {
        List<List<TreeCommand<S>>> branches = new ArrayList<>();
        if (this.children.size() == 0 || this.getRun() != null) {
            branches.add(new ArrayList<>(List.of(this)));
        }
        for (TreeCommand<S> child : this.children) {
            if (child.hasNoPermission(source)) continue;
            List<List<TreeCommand<S>>> childBranches = child.getBranches(source);
            for (List<TreeCommand<S>> childBranch : childBranches) {
                childBranch.add(0, this);
                branches.add(childBranch);
            }
        }
        return branches;
    }

    public boolean hasNoPermission(S source) {
        if (this.permission == null) return false;
        return !this.command.hasPermission(source, this.permission);
    }

    public abstract List<String> getSuggestions(ParseContext<S> context);

    public abstract TextComponent getHelpComponent();

    public abstract TextComponent getHelpExtraDescription();
}
