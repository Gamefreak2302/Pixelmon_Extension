package io.gamefreak.pixelmonextension.token;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class SpeedIvToken extends PixelmonToken {

    public SpeedIvToken(){
        this.displayName = "&AMax Speed token";
        this.name = TokenTypes.TokenName.MaxSpeedIvs;
        this.setInfo();
        //this.item = createItem(ItemTypes.NETHER_STAR);
    }

    @Override
    public boolean checkValid(Pokemon pokemon, Player player) {
        if(pokemon.getStats().ivs.speed == 31){
            player.sendMessage(Text.of(TextColors.RED,"Speed already has max ivs"));
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

        pokemon.getIVs().set(StatsType.Speed,31);
        player.sendMessage(Text.of(TextColors.GREEN, pokemon.getDisplayName() + " now has max speed ivs"));
    }

    @Override
    public String info() {
        return "Turn the Speed ivs of the player to max ivs";
    }
}
