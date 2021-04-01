package io.gamefreak.pixelmonextension.token;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RandomSizeToken extends PixelmonToken {


    public RandomSizeToken(){
        this.displayName="&cRandom size token";
        this.name = TokenTypes.TokenName.RandomSize;
        this.setInfo();
        //this.item = createItem(ItemTypes.NETHER_STAR);
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

        List<EnumGrowth> sizes =Arrays.asList(EnumGrowth.values());
        Collections.shuffle(sizes);
        EnumGrowth size;
        if(sizes.get(0) == pokemon.getGrowth()){
            size = sizes.get(1);
        }
        else{
            size = sizes.get(0);
        }

        player.sendMessage(Text.of(TextColors.GREEN,pokemon.getDisplayName() + "has now a size of " + size.name()));
        pokemon.setGrowth(size);
    }

    @Override
    public String info() {
        return "Changes the pokemons size to a random size";
    }
}
