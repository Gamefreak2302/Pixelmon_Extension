package io.gamefreak.pixelmonextension.token;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NatureToken extends PixelmonToken {

    public NatureToken(){
        this.displayName = "&aNature Token";
        this.name = TokenTypes.TokenName.Nature;
        this.setInfo();
    }
    @Override
    public boolean checkValid(Pokemon pokemon, Player player) {
        if(pokemon.getOwnerPlayerUUID() != player.getUniqueId()){
            player.sendMessage(Text.of(TextColors.RED,"This is not your pokemon"));
            return false;
        }
        if(!TokenConfigSettings.allowModification(pokemon.getSpecies())){
            player.sendMessage(Text.of(TextColors.RED,pokemon.getDisplayName() + " can not be modified."));
            return false;
        }
        return true;
    }

    @Override
    public void activate(Pokemon pokemon, Player player) {

        List<EnumNature> natures = Arrays.asList(EnumNature.values());
        Collections.shuffle(natures);

        EnumNature nature = natures.get(0);
        if(pokemon.getNature() == nature){
            nature = natures.get(1);
        }

        pokemon.setNature(nature);
        player.sendMessage(Text.of(TextColors.GREEN,pokemon.getDisplayName() + " now has the nature " + nature.name()));
    }

    @Override
    public String info() {
        return "Change the nature of the pokemon to a random nature different from the current nature";
    }
}
