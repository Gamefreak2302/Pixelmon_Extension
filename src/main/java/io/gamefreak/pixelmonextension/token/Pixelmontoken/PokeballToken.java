package io.gamefreak.pixelmonextension.token.Pixelmontoken;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PokeballToken extends PixelmonToken {

    public PokeballToken(){
        this.name = TokenTypes.TokenName.Pokeball;
        this.displayName = "&aPokeball token";
        //this.item = this.createItem(ItemTypes.NETHER_STAR);
        this.setInfo();
    }
    @Override
    public boolean checkValid(Pokemon pokemon, Player player) {
        if(pokemon.getOwnerPlayerUUID() != player.getUniqueId()){
            player.sendMessage(Text.of(TextColors.RED,"This pokemon is not yours."));
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

        List<EnumPokeballs> pokeballs = Arrays.asList(EnumPokeballs.values());
        Collections.shuffle(pokeballs);
        EnumPokeballs current;
        if(pokemon.getCaughtBall() == pokeballs.get(0)){
            current = pokeballs.get(1);
        }else{
            current = pokeballs.get(0);
        }

        pokemon.setCaughtBall(current);
        player.sendMessage(Text.of(TextColors.GREEN,pokemon.getDisplayName() + " has now been caught in " + current.name()));
    }

    @Override
    public List<Text> info() {
        return Arrays.asList(Text.of(TextColors.DARK_PURPLE,"Right-click a Pokemon to change their pokeball"),Text.of(TextColors.DARK_GRAY,"token id:" + this.name.name()));

    }
}
