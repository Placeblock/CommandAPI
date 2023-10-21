package de.codelix.commandapi.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.design.CommandDesign;
import de.codelix.commandapi.core.parser.ParsedCommandBranch;
import de.codelix.commandapi.minecraft.MCCommandAudience;
import de.codelix.commandapi.minecraft.MCCommandBridge;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractVelocityCommandAdapter<P> extends Command<MCCommandAudience<P>> implements RawCommand, MCCommandBridge<P, MCCommandAudience<P>>  {

    public AbstractVelocityCommandAdapter(String label, boolean async, CommandDesign commandDesign) {
        super(label, async, commandDesign);
    }

    @Override
    public void execute(Invocation invocation) {
        MCCommandAudience<P> commandAudience = new MCCommandAudience<>(invocation.source(), this.getPrefix());
        this.parseAndExecute(invocation.alias() + invocation.arguments(), commandAudience);
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        MCCommandAudience<P> commandAudience = new MCCommandAudience<>(invocation.source(), this.getPrefix());
        List<ParsedCommandBranch<MCCommandAudience<P>>> parsed = this.parse(invocation.alias() + invocation.arguments(), commandAudience);
        return this.getSuggestions(parsed, commandAudience);
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        MCCommandAudience<P> commandAudience = new MCCommandAudience<>(invocation.source(), this.getPrefix());
        List<ParsedCommandBranch<MCCommandAudience<P>>> parsed = this.parse(invocation.alias() + invocation.arguments(), commandAudience);
        CompletableFuture<List<String>> completableFuture = new CompletableFuture<>();
        completableFuture.complete(this.getSuggestions(parsed, commandAudience));
        return completableFuture;
    }

    @Override
    public void init() {

    }

    @Override
    public void register() {

    }

    @Override
    public void unregister() {

    }
}
