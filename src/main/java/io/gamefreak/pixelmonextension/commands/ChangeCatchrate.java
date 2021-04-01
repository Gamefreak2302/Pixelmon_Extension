package io.gamefreak.pixelmonextension.commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import io.gamefreak.pixelmonextension.Pixelmonextension;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ChangeCatchrate implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        CommandResult success = CommandResult.success();

        EnumSpecies ES =(EnumSpecies) args.getOne("species").orElse(null);
        int rate = (Integer)args.getOne("rate").orElse(-1);

        if(ES == null){
            src.sendMessage(Text.of(TextColors.RED,"Pokemon can not be empty"));
            return success;
        }

        if(rate == -1){
            src.sendMessage(Text.of(TextColors.RED,"Catch rate can not be empty"));
            return success;
        }

        if(rate < 1){
            src.sendMessage(Text.of(TextColors.RED,"Catch rate can not be lower then 1"));
        }

        Pixelmonextension.registry.addCatchrate(ES,rate);
        Pixelmonextension.db.UpdateCatchrate();
        src.sendMessage(Text.of(TextColors.GREEN, ES.name + " now has catchrate " + rate));
        return success;
    }
}
