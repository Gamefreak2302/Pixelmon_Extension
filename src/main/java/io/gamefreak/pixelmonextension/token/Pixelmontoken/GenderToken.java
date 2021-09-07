package io.gamefreak.pixelmonextension.token.Pixelmontoken;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.List;

public class GenderToken extends PixelmonToken{


    public GenderToken() {
        this.displayName = "&aChange Gender Token";
        this.name = TokenTypes.TokenName.Gender;
        this.setInfo();
        //this.item = createItem(ItemTypes.TOTEM_OF_UNDYING);

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
        if(pokemon.getGender() == Gender.None){
            player.sendMessage(Text.of( TextColors.RED,"Gender can not be switched, because pokemon has no gender."));
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

        Gender gender = pokemon.getGender();
        pokemon.setGender(pokemon.getGender() == Gender.Male?Gender.Female:Gender.Male);
        if(pokemon.getGender() == gender){
            player.sendMessage(Text.of(TextColors.RED,pokemon.getDisplayName() + " only has 1 gender type"));
            PixelmonToken token = new GenderToken();
            player.getInventory().offer(token.item);
        }else{
            player.sendMessage(Text.of(TextColors.GREEN,pokemon.getDisplayName() + " is now " + pokemon.getGender()));
        }
    }
    @Override
    public List<Text> info() {
        return Arrays.asList(Text.of(TextColors.DARK_PURPLE,"Right-click a Pokemon to change their gender"),Text.of(TextColors.DARK_GRAY,"token id:" + this.name.name()));

    }
}
