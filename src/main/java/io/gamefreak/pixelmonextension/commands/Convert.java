package io.gamefreak.pixelmonextension.commands;

import io.gamefreak.pixelmonextension.Pixelmonextension;
import io.gamefreak.pixelmonextension.token.Token;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Map;

public class Convert implements CommandExecutor {


    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        CommandResult success = CommandResult.success();
        Player player = src instanceof Player ? (Player) src : null;
        TokenTypes.TokenName tokenname = (TokenTypes.TokenName) args.getOne("token").orElse(null);
        int amount = (Integer) args.getOne("amount").orElse(1);

        if (player == null) {
            src.sendMessage(Text.of(TextColors.RED, "Only players can execute this command"));
            return success;
        }

        if (amount < 1) {
            player.sendMessage(Text.of(TextColors.RED, "amount must be atleast 1"));
            return success;
        }
        Map<TokenTypes.TokenName, Integer> balance = Pixelmonextension.registry.getUnclaimedTokens(player.getUniqueId());
        if (tokenname != null) { // to physical
            Token token = TokenTypes.getTokenFromTokenName(tokenname);
            ItemStack st = token.getItem();
            st.setQuantity(amount);
            if (balance.get(tokenname) == null || balance.get(tokenname) < amount) {
                player.sendMessage(Text.of(TextColors.RED, "You don't have enough " + tokenname + " tokens"));
                return success;
            }
            if (!player.getInventory().first().canFit(st)) {
                player.sendMessage(Text.of(TextColors.RED, "You don't have enough space in your inventory for " + amount + " " + tokenname + " " + (amount != 1 ? "tokens" : "token")));
                return success;
            }

            player.getInventory().first().offer(st);
            balance.put(tokenname, balance.get(tokenname) - amount);
            Pixelmonextension.db.UpdateTokenOfUuid(player.getUniqueId());
            player.sendMessage(Text.of(TextColors.GREEN, "You successfully converted " + amount + " " + tokenname.name() + " " + (amount != 1 ? "tokens" : "token")));
            Pixelmonextension.INSTANCE.logger.info(player.getName() + " has successfully converted " + amount + " " + tokenname.name() + " " + (amount != 1 ? "tokens" : "token") + " to physical " + (amount != 1 ? "keys" : "key"));
            return success;
        }

        ItemStack stack = player.getItemInHand(HandTypes.MAIN_HAND).orElse(null);
        if (stack == null || stack.getType() == ItemTypes.AIR) {
            player.sendMessage(Text.of(TextColors.RED, "Your main hand is empty"));
            return success;
        }
        if (!TokenTypes.isToken(stack)) {
            player.sendMessage(Text.of(TextColors.RED, "Item in main hand is not a valid token"));
            return success;
        }


        String tokentype = stack.toContainer().getString(DataQuery.of("UnsafeData", "TokenType")).isPresent() ? stack.toContainer().getString(DataQuery.of("UnsafeData", "TokenType")).get() : null;
        TokenTypes.TokenName name = TokenTypes.getTokenNameFromString(tokentype);
        if (name != null) {
            if (amount > stack.getQuantity()) {
                player.sendMessage(Text.of(TextColors.RED, "You don't have that many tokens in your hand"));
                return success;
            }
            Pixelmonextension.registry.addUnclaimedToken(player.getUniqueId(), name, amount);
            Pixelmonextension.db.UpdateTokenOfUuid(player.getUniqueId());
            stack.setQuantity(stack.getQuantity() - amount);
            player.sendMessage(Text.of(TextColors.GREEN, "You have converted " + amount + " " + name.name() + " " + (amount != 1 ? "tokens" : "token")));
            Pixelmonextension.INSTANCE.logger.info(player.getName() + " has converted " + amount + " " + name.name() + " " + (amount != 1 ? "tokens" : "token" + " to virtual " + (amount != 1 ? "keys" : "key")));
            return success;
        } else {
            player.sendMessage(Text.of(TextColors.RED, "Item in hand is not a valid token"));
            return success;
        }

    }
}
