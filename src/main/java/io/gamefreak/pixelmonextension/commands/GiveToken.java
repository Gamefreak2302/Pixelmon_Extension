package io.gamefreak.pixelmonextension.commands;

import io.gamefreak.pixelmonextension.Pixelmonextension;
import io.gamefreak.pixelmonextension.token.Token;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;


public class GiveToken implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        User player = (User) args.getOne("player").orElse(null);
        TokenTypes.TokenName tk = (TokenTypes.TokenName) args.getOne("token").orElse(null);
        int amount = (int) args.getOne("amount").orElse(1);
        Token token;

        if(src instanceof Player){
            if(player == null){
                player = (Player) src;
            }

        }

        CommandResult success = CommandResult.success();
        if(player == null){
            src.sendMessage(Text.of(TextColors.RED, "Could not deliver token to unknown/unset player"));
            return success;

        }

        if(amount < 1){
            src.sendMessage(Text.of(TextColors.RED, "Must atleast give 1 token"));
            return success;
        }

        if(tk == null){

            src.sendMessage(Text.of(TextColors.RED,"Token can not be empty or does not exist"));
            return success;
        }


        token = TokenTypes.getTokenFromTokenName(tk);

        if(token == null){
            src.sendMessage(Text.of(TextColors.RED,"Invalid token"));
            return success;
        }
        token.getItem().setQuantity(amount);

        if(player.getInventory().first().canFit(token.getItem()) ){
            if (player.isOnline()) {
                player.getInventory().offer(token.getItem());
                player.getPlayer().get().sendMessage(Text.of(TextColors.GREEN, "You received " + token.getName().name() + " Tokens"));
            }
                else{
                Pixelmonextension.registry.addUnclaimedToken(player.getUniqueId(),token.getName(),amount);
                Pixelmonextension.db.UpdateTokenOfUuid(player.getUniqueId());
                src.sendMessage(Text.of(TextColors.GREEN, token.getName().name() + " token(s) sent"));
            }
        }else{
            src.sendMessage(Text.of(TextColors.RED,"Inventory of " + player.getName() + " is full. Adding to token list"));
            Pixelmonextension.registry.addUnclaimedToken(player.getUniqueId(),token.getName(),amount);
            if (player.isOnline())
                player.getPlayer().get().sendMessage(Text.of(TextColors.RED,"Some tokens were sent to your token list, please use /token balance redeem to redeem"));
            Pixelmonextension.db.UpdateTokenOfUuid(player.getUniqueId());

        }
        return CommandResult.success();
    }
}
