package io.gamefreak.pixelmonextension.token;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.List;

public class AtkIvToken extends PixelmonToken {

    public AtkIvToken(){
        this.displayName = "&aMax Attack token";
        this.name = TokenTypes.TokenName.MaxAtkIvs;
        this.setInfo();
        //this.item = createItem(ItemTypes.NETHER_STAR);
    }

    @Override
    public boolean checkValid(Pokemon pokemon, Player player) {
        if(pokemon.getStats().ivs.attack == 31){
            player.sendMessage(Text.of(TextColors.RED,"Attack already has max ivs"));
            return false;
        }
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

        pokemon.getIVs().set(StatsType.Attack,31);
        player.sendMessage(Text.of(TextColors.GREEN, pokemon.getDisplayName() + " now has max attack ivs"));
    }

    @Override
    public List<Text> info() {
        return Arrays.asList(Text.of(TextColors.DARK_PURPLE,"Right-click a Pokemon to maximize their attack IV"),Text.of(TextColors.DARK_GRAY,"token id:" + this.name.name()));

    }
}
