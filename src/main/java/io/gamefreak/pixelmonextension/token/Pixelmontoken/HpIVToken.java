package io.gamefreak.pixelmonextension.token.Pixelmontoken;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.List;

public class HpIVToken extends PixelmonToken{


    public HpIVToken(){
        this.displayName = "&aMax HP token";
        this.name = TokenTypes.TokenName.MaxHPIvs;
        this.setInfo();
    }

    @Override
    public boolean checkValid(Pokemon pokemon, Player player) {
        if(pokemon.getStats().ivs.hp == 31){
            player.sendMessage(Text.of(TextColors.RED,"Hp already has max ivs"));
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

        pokemon.getIVs().set(StatsType.HP,31);
        player.sendMessage(Text.of(TextColors.GREEN, pokemon.getDisplayName() + " now has max hp ivs"));
    }

    @Override
    public List<Text> info() {
        return Arrays.asList(Text.of(TextColors.DARK_PURPLE,"Right-click a Pokemon to maximize their hp IV"),Text.of(TextColors.DARK_GRAY,"token id:" + this.name.name()));

    }
}
