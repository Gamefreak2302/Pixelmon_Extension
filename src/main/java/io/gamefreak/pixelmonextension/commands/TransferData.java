package io.gamefreak.pixelmonextension.commands;

import io.gamefreak.pixelmonextension.storage.Database;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

public class TransferData implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        Database.transferData();
        return CommandResult.success();
    }
}
