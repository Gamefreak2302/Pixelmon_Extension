package io.gamefreak.pixelmonextension.commands;

import io.gamefreak.pixelmonextension.token.PixelmonToken;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class Info implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        TokenTypes.TokenName name = (TokenTypes.TokenName) args.getOne("token").orElse(null);
        if(name == null){
            src.sendMessage(Text.of(TextColors.RED,"No tokenname given"));
            return CommandResult.success();
        }

        PixelmonToken token = TokenTypes.getTokenFromTokenName(name);
        if(token == null){
            src.sendMessage(Text.of(TextColors.RED,"Failed to find the correct token, please report this to an administrator"));
            return CommandResult.success();
        }
        src.sendMessage(Text.of(TextColors.YELLOW , "=== " + name.name() + " ==="));
        src.sendMessage(Text.of(TextColors.YELLOW, token.info()));

        return CommandResult.success();
    }
}
