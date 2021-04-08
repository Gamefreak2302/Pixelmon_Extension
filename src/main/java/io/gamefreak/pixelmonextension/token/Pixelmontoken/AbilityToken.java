package io.gamefreak.pixelmonextension.token.Pixelmontoken;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.List;


public class AbilityToken extends PixelmonToken{

    public AbilityToken(){

        this.displayName = "&aAbilityToken";
        this.name = TokenTypes.TokenName.Ability;
        this.setInfo();
        //this.item = createItem(ItemTypes.NETHER_STAR);
    }
    @Override
    public boolean checkValid(Pokemon pokemon, Player player) {
        if(pokemon.getOwnerPlayerUUID() != player.getUniqueId()){
            player.sendMessage(Text.of(TextColors.RED,"This is not your pokemon"));
            return false;
        }
        if(pokemon.getBaseStats().abilities.length == 1){
            player.sendMessage(Text.of(TextColors.RED, pokemon.getDisplayName() + " does not have a different ability"));
            return false;
        }
        String[] abilities = pokemon.getBaseStats().abilities;
        // check if pokemon has 2 Non-hidden abilities
        if((abilities[0] == null ||abilities[1] == null) && !pokemon.getAbility().getName().equalsIgnoreCase(abilities[2])){
            player.sendMessage(Text.of(TextColors.RED,pokemon.getDisplayName() + " does not have a different non-hidden ability"));
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

        AbilityBase ab = null;
        int number = -1;
        if(pokemon.getAbilitySlot() == 0){
            number = 1;
            ab = AbilityBase.getAbility(pokemon.getBaseStats().abilities[1]).get();
        }else{
            number = 0;
            ab = AbilityBase.getAbility(pokemon.getBaseStats().abilities[0]).get();
        }

        pokemon.setAbilitySlot(number);
        player.sendMessage(Text.of(TextColors.GREEN,pokemon.getDisplayName() + " now has the ability " + ab.getName()));

    }

    @Override
    public List<Text> info() {
        return Arrays.asList(Text.of(TextColors.DARK_PURPLE,"Right-click a Pokemon to change it's ability."),Text.of(TextColors.DARK_GRAY,"token id:" + this.name.name()));

    }
}
