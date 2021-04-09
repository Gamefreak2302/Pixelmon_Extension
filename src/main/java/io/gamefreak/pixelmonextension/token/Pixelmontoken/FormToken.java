package io.gamefreak.pixelmonextension.token.Pixelmontoken;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FormToken extends PixelmonToken{

    private List<IEnumForm> forms;
    public FormToken(){
        this.displayName = "&aForm token";
        this.name = TokenTypes.TokenName.Form;
        this.setInfo();
        //this.item = this.createItem(ItemTypes.NETHER_STAR);
    }
    @Override
    public boolean checkValid(Pokemon pokemon, Player player) {

        EnumSpecies[] species = {EnumSpecies.Zygarde,EnumSpecies.Kyogre,EnumSpecies.Groudon};
        List<EnumSpecies> blacklist = Arrays.asList(species);
        if(pokemon.getOwnerPlayerUUID() != player.getUniqueId()){
            player.sendMessage(Text.of(TextColors.RED,"This pokemon is not yours."));
            return false;
        }
        if(!TokenConfigSettings.allowModification(pokemon.getSpecies())){
            player.sendMessage(Text.of(TextColors.RED,pokemon.getDisplayName() + " can not be modified."));
            return false;
        }

        forms = pokemon.getSpecies().getPossibleForms(false);
        IEnumForm temp = forms.stream().filter(s -> s.getLocalizedName().equalsIgnoreCase("giveaway")).findFirst().orElse(null);
        if(temp != null){
            forms.remove(temp);
        }
        if(pokemon.getSpecies().getPossibleForms(false).size() == 1 ){
            player.sendMessage(Text.of(TextColors.RED,pokemon.getDisplayName() + " does not have any other forms"));
            return false;
        }
        if( blacklist.contains(pokemon.getSpecies())){
            player.sendMessage(Text.of(TextColors.RED,pokemon.getDisplayName() + " does not have any other valid forms"));
            return false;
        }
        return true;
    }

    @Override
    public void activate(Pokemon pokemon, Player player) {


        IEnumForm form;
        Collections.shuffle(forms);
        if(forms.get(0) == pokemon.getFormEnum()){
            form= forms.get(1);
        }else{
            form= forms.get(0);
        }
        pokemon.setForm(form);
        String formname = form.getLocalizedName().equalsIgnoreCase("None")?"Normal":form.getLocalizedName();
        player.sendMessage(Text.of(TextColors.GREEN,pokemon.getDisplayName() + " now has form" + formname ));

    }

    @Override
    public List<Text> info() {
        return Arrays.asList(Text.of(TextColors.DARK_PURPLE,"Right-click a Pokemon to change their form"),Text.of(TextColors.DARK_GRAY,"token id:" + this.name.name()));

    }
}
