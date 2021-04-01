package io.gamefreak.pixelmonextension.commands;

import io.gamefreak.pixelmonextension.Pixelmonextension;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.stream.Collectors;

public class ReadCatchrate implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        CommandResult success = CommandResult.success();

        if (Pixelmonextension.registry.getCatchrates() == null || Pixelmonextension.registry.getCatchrates().size() == 0) {
            src.sendMessage(Text.of(TextColors.RED, "No changed catchrates"));
            return success;
        }

        src.sendMessage(Text.of(TextColors.YELLOW, "=== Changed rates ==="));
        src.sendMessage(Text.of(TextColors.YELLOW, Pixelmonextension.registry.getCatchrates().entrySet()
                .stream().map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining("  "))));

        return success;
    }
}
