package io.gamefreak.pixelmonextension.token.Pixelmontoken;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.List;

public class EvsResetToken extends PixelmonToken{

    public EvsResetToken(){

        this.displayName = "&aReset EVS token";
        this.name = TokenTypes.TokenName.ResetEvs;
        this.setInfo();
    }

    @Override
    public boolean checkValid(Pokemon pokemon, Player player) {

        if(pokemon.getOwnerPlayerUUID() != player.getUniqueId()){
            player.sendMessage(Text.of(TextColors.RED,"This is not your pokemon"));
            return false;
        }
        if(pokemon.getEVs().getStat(StatsType.Attack)==0 &&
                pokemon.getEVs().getStat(StatsType.Defence)==0 &&
                pokemon.getEVs().getStat(StatsType.SpecialAttack)==0 &&
                pokemon.getEVs().getStat(StatsType.SpecialDefence)==0 &&
                pokemon.getEVs().getStat(StatsType.Speed)==0 &&
                pokemon.getEVs().getStat(StatsType.HP)==0 ){
            player.sendMessage(Text.of(TextColors.RED,"The pokemon has no EVS yet"));
            return false;
        }

        return true;
    }

    @Override
    public void activate(Pokemon pokemon, Player player) {
        pokemon.getStats().evs.setStat(StatsType.Attack,0);
        pokemon.getStats().evs.setStat(StatsType.Defence,0);
        pokemon.getStats().evs.setStat(StatsType.Speed,0);
        pokemon.getStats().evs.setStat(StatsType.SpecialAttack,0);
        pokemon.getStats().evs.setStat(StatsType.SpecialDefence,0);
        pokemon.getStats().evs.setStat(StatsType.HP,0);
        player.sendMessage(Text.of(TextColors.GREEN,"The pokemon's evs resetted"));

    }

    @Override
    public List<Text> info() {
        return Arrays.asList(Text.of(TextColors.DARK_PURPLE,"Right-click a Pokemon to reset their EVS"),Text.of(TextColors.DARK_GRAY,"token id:" + this.name.name()));
    }
}
