package io.gamefreak.pixelmonextension.token.Pixelmontoken;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.List;

public class HiddenAbilityToken extends PixelmonToken {

    public HiddenAbilityToken(){
        this.displayName = "&aHidden ability token";
        this.name = TokenTypes.TokenName.HiddenAbility;
        this.setInfo();
        //this.item = createItem(ItemTypes.NETHER_STAR);
    }

    @Override
    public boolean checkValid(Pokemon pokemon, Player player) {

        if(pokemon.getOwnerPlayerUUID() != player.getUniqueId()){
            player.sendMessage(Text.of(TextColors.RED,"This is not your pokemon"));
            return false;
        }

        if(pokemon.getAbility().getName().equalsIgnoreCase(pokemon.getBaseStats().abilities[2])){
            player.sendMessage(Text.of(TextColors.RED,pokemon.getDisplayName() + " does already have his hidden ability"));
            return false;
        }
        if(pokemon.getBaseStats().abilities[2] == null){
            player.sendMessage(Text.of(TextColors.RED,pokemon.getDisplayName() + " does not have a hidden ability"));
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

        pokemon.setAbilitySlot(2);
        player.sendMessage(Text.of(TextColors.GREEN,pokemon.getDisplayName() + " now has a hidden ability"));
    }

    @Override
    public List<Text> info() {
        return Arrays.asList(Text.of(TextColors.DARK_PURPLE,"Right-click a Pokemon to give it his Hidden ability"),Text.of(TextColors.DARK_GRAY,"token id:" + this.name.name()));

    }
}
