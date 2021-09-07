package io.gamefreak.pixelmonextension.commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.IStorageManager;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import io.gamefreak.pixelmonextension.token.SpawnTokens.SpawnTokenLists;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collections;
import java.util.List;

public class RandomEgg implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        CommandResult success = CommandResult.success();


        Player player = (Player) args.getOne("target").orElse(null);
        if(player == null){
            if(!(src instanceof  Player)){
                src.sendMessage(Text.of(TextColors.RED,"Player can not be empty"));
                return success;
            }else{
                player = (Player) src;
            }
        }



        List<EnumSpecies> choices = SpawnTokenLists.getRegularList();
        Collections.shuffle(choices);
        Pokemon pokemon = Pixelmon.pokemonFactory.create(choices.get(0));
        pokemon.makeEgg();

        IStorageManager manager = Pixelmon.storageManager;
        if(manager.getParty(player.getUniqueId()).hasSpace()){
            manager.getParty(player.getUniqueId()).add(pokemon);
            player.sendMessage(Text.of(TextColors.GRAY,pokemon.getDisplayName() + " has been added to your party"));

        }else{
            manager.getPCForPlayer(player.getUniqueId()).add(pokemon);
            player.sendMessage(Text.of(TextColors.GRAY,pokemon.getDisplayName() + " has been added to your pc"));
        }

        return success;
    }
}
