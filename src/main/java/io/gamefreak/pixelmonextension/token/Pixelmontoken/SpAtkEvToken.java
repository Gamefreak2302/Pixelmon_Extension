package io.gamefreak.pixelmonextension.token.Pixelmontoken;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.List;

public class SpAtkEvToken extends PixelmonToken {

    public SpAtkEvToken(){
        this.displayName = "&AMax Special attack evs token";
        this.name = TokenTypes.TokenName.MaxSpAtkEvs;
        this.setInfo();
        //this.item = createItem(ItemTypes.NETHER_STAR);
    }

    @Override
    public boolean checkValid(Pokemon pokemon, Player player) {
        if(pokemon.getStats().evs.getStat(StatsType.SpecialAttack) == 252){
            player.sendMessage(Text.of(TextColors.RED,"Special attack already has max evs"));
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
        if(getBlacklist().contains(pokemon.getSpecies())){
            player.sendMessage(Text.of(TextColors.RED,pokemon.getDisplayName() + " can not be modified with this token"));
            return false;
        }
        return true;
    }

    @Override
    public void activate(Pokemon pokemon, Player player) {

        pokemon.getEVs().setStat(StatsType.SpecialAttack,252);
        player.sendMessage(Text.of(TextColors.GREEN, pokemon.getDisplayName() + " now has max special attack evs"));
    }

    @Override
    public List<Text> info() {
        return Arrays.asList(Text.of(TextColors.DARK_PURPLE,"Right-click a Pokemon to maximize their special attack EV"),Text.of(TextColors.DARK_GRAY,"token id:" + this.name.name()));

    }
}
