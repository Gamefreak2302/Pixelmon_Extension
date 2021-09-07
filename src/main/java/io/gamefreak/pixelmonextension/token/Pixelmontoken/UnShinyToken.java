package io.gamefreak.pixelmonextension.token.Pixelmontoken;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import io.gamefreak.pixelmonextension.token.TokenTypes.TokenName;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.List;

public class UnShinyToken extends PixelmonToken{


    public UnShinyToken() {
        this.name = TokenName.Normalize;
        this.displayName = "&7Normalize token";
        this.setInfo();
        // this.item = createItem(ItemTypes.NETHER_STAR);




    }

    @Override
    public boolean checkValid(Pokemon pokemon, Player player) {

        if(!pokemon.isShiny()) {
            player.sendMessage(Text.of(TextColors.RED,"This pokemon is not shiny."));
            return false;
        }
        if(pokemon.getOwnerPlayerUUID() != player.getUniqueId()){
            player.sendMessage(Text.of(TextColors.RED,"This pokemon is not yours."));
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

        player.sendMessage(Text.of(TextColors.GREEN ,pokemon.getDisplayName() + " is no longer shiny"));
        pokemon.setShiny(false);

    }

    @Override
    public List<Text> info() {
        return Arrays.asList(Text.of(TextColors.DARK_PURPLE,"Right-click a Pokemon to turn it non-shiny"),Text.of(TextColors.DARK_GRAY,"token id:" + this.name.name()));

    }
}
