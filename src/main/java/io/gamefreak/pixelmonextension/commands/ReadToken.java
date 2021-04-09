package io.gamefreak.pixelmonextension.commands;

import io.gamefreak.pixelmonextension.Pixelmonextension;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Map;

public class ReadToken implements CommandExecutor {


    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {

        CommandResult success = CommandResult.success();


        //User player = (User) args.getOne("player").orElse(null);
        Text header;
        User us = (User) args.getOne("player").orElse(null);
        User user;
        if(us != null){

            user = us;
            if(!src.hasPermission("pixelmonextension.balance.others")){
                src.sendMessage(Text.of(TextColors.RED,"You don't have the permission to see others tokens"));
                return CommandResult.success();
            }
            header = Text.of(TextColors.YELLOW, " === " + user.getName() + "\'s tokens ===") ;
        }else{
            if (src instanceof User){
                user = (User) src;
                header = Text.of(TextColors.YELLOW,"=== Tokens ===");
            }else{
                src.sendMessage(Text.of(TextColors.RED,"User is not present"));
                return CommandResult.success();
            }
        }

        src.sendMessage(Text.of(TextColors.YELLOW,header));
        Map<TokenTypes.TokenName,Integer> tokens = Pixelmonextension.registry.getUnclaimedTokens(user.getUniqueId());
        for(Map.Entry<TokenTypes.TokenName,Integer> entry :tokens.entrySet()){
            src.sendMessage(Text.of(TextColors.YELLOW, entry.getKey() + ":" + entry.getValue()));
        }


        return success;
    }


}
