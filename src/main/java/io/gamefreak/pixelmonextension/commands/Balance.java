package io.gamefreak.pixelmonextension.commands;

import io.gamefreak.pixelmonextension.Pixelmonextension;
import io.gamefreak.pixelmonextension.token.Token;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.HoverAction;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Map;

public class Balance implements CommandExecutor {


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
            if(entry.getValue() != 0){
                Token token = TokenTypes.getTokenFromTokenName(entry.getKey());

                if(us == null){
                Text text = Text.of(TextSerializers.FORMATTING_CODE.deserialize("&6- " +  token.getDisplayName() + " &6: " + entry.getValue() ))
                        .toBuilder()
                        .onClick(TextActions.executeCallback(r -> {
                            if(entry.getValue() > 0 ){
                                ItemStack stack = ItemStack.builder().from(token.getItem()).quantity(1).build();
                                if(user.getInventory().first().canFit(stack)){
                                    user.getInventory().first().offer(stack);
                                    entry.setValue(entry.getValue()-1);
                                    Pixelmonextension.db.UpdateTokenOfUuid(user.getUniqueId());
                                    //Pixelmonextension.registry.addToken(user.getUniqueId(),entry.getKey(),1);
                                    src.sendMessage(Text.of(TextColors.GREEN,"You converted a " + entry.getKey() + " token"));
                                    Sponge.getCommandManager().process(user.getPlayer().get(),"token bal");
                                }else{
                                    src.sendMessage(Text.of(TextColors.RED,"Not enough inventory space to add a " + entry.getKey() + "token"));
                                }
                            }else{
                                src.sendMessage(Text.of(TextColors.RED,"Not enough keys"));
                            }
                        }))
                        .onHover(
                            TextActions.showText(Text.of(TextColors.GOLD, "Click to convert 1 " + entry.getKey().name() + " token"))
                        ).build();
                src.sendMessage(text);



            }else{
                    Text text = Text.of(TextSerializers.FORMATTING_CODE.deserialize("&6- " +  token.getDisplayName() + " &6: " + entry.getValue() ));
            }
            }
        }


        return success;
    }


}
