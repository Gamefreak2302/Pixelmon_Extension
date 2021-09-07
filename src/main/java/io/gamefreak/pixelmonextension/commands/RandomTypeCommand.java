package io.gamefreak.pixelmonextension.commands;

import com.codehusky.huskycrates.HuskyCrates;
import com.codehusky.huskycrates.crate.virtual.Crate;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.IStorageManager;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RandomTypeCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        // variables for the command + giving default values
        EnumType type = (EnumType)args.getOne("type").orElse(null);
        Player player= (Player)args.getOne("player").orElse(null);
        Crate crate = (Crate) args.getOne("crate").orElse(null);
        // check if player is empty (null)
        if(player == null){
            // check if executor is a player, if not tells player can not be empty
            if(src instanceof Player){
                player = (Player) src;
            }else{
                src.sendMessage(Text.of(TextColors.RED,"Player can not be empty"));
                return CommandResult.success();
            }
        }
        // check if crate is given
        if(crate == null){
            src.sendMessage(Text.of(TextColors.RED,"Crate can not be empty"));
            return CommandResult.success();
        }
        // check if huskycrates knows crate
        if(!HuskyCrates.registry.getCrates().containsKey(crate.getId())){
            src.sendMessage(Text.of(TextColors.RED,"Crate does not exist"));
            return CommandResult.success();
        }
        // stop command and send message if type is not given (chance of throwing is small as it's already required)
        if(type == null){
            src.sendMessage(Text.of(TextColors.RED,"Type is invalid"));
            return CommandResult.success();
        }
        // create list from given type
        List<EnumSpecies> species = Arrays.stream(EnumSpecies.values())
                .filter(s -> s.getBaseStats().types.contains(type))
                .filter(s -> !s.isUltraBeast())
                .filter(s -> !s.isLegendary())
                .collect(Collectors.toList());


        // shuffle list
        Collections.shuffle(species);
        // get first pokemon from list
        Pokemon pokemon = Pixelmon.pokemonFactory.create(species.get(0));
        // create pokemon storage manager, to choose where to add the pokemon
        IStorageManager manager = Pixelmon.storageManager;
        // give pokemon to player ( to pc if party is full)
        if(manager.getParty(player.getUniqueId()).hasSpace()){
            manager.getParty(player.getUniqueId()).add(pokemon);
        }else{
            manager.getPCForPlayer(player.getUniqueId()).add(pokemon);
        }
        // Broadcast player has won a <pokemon> from a <crate> crate
        Sponge.getServer().getBroadcastChannel().send(Text.of(TextSerializers.FORMATTING_CODE
                .deserialize("&a" + player.getName() + " has won a "+ pokemon.getDisplayName()
                        + " from a "
                        + (crate.getName().toLowerCase().endsWith("crate") ?
                        crate.getName() + ".": (crate.getName()+ " crate."))) ));

        return CommandResult.success();
    }
}
