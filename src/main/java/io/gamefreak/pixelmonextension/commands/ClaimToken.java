package io.gamefreak.pixelmonextension.commands;

import io.gamefreak.pixelmonextension.Pixelmonextension;
import io.gamefreak.pixelmonextension.token.PixelmonToken;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ClaimToken implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        CommandResult success= CommandResult.success();

        int amount = (int) args.getOne("amount").orElse(1);
        TokenTypes.TokenName tokenname = (TokenTypes.TokenName) args.getOne("token").orElse(null);
        PixelmonToken token = null;
        if(tokenname != null){
            token = TokenTypes.getTokenFromTokenName(tokenname);
        }

        if(src instanceof Player){
            Player player = (Player) src;

            if(tokenname == null){
                player.sendMessage(Text.of(TextColors.RED,"Token can not be empty"));
                return success;
            }

            if(amount < 0){
                player.sendMessage(Text.of(TextColors.RED, "Amount can not be less then 1"));
                return success;
            }
            if(amount > 64){
                player.sendMessage(Text.of(TextColors.RED,"Amount must be lower then 65"));
                return success;
            }

            if(Pixelmonextension.registry.claimToken(player.getUniqueId(),tokenname,amount)){
                player.getInventory().first().offer(ItemStack.builder().from(token.getItem()).quantity(amount).build());
                Pixelmonextension.db.UpdatePixelmonTokenOfUuid(player.getUniqueId());
                return success;
            }
        }

        return success;
    }
}
