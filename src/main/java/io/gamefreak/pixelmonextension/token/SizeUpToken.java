package io.gamefreak.pixelmonextension.token;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.List;

public class SizeUpToken extends PixelmonToken{

    public SizeUpToken(){
        this.name = TokenTypes.TokenName.SizeUp;
        this.displayName = "Growth UP token";
        this.setInfo();
        //this.item = createItem(ItemTypes.REDSTONE_TORCH);
    }
    @Override
    public boolean checkValid(Pokemon pokemon, Player player) {

        if(pokemon.getGrowth() == EnumGrowth.Ginormous){
            player.sendMessage(Text.of(TextColors.RED, pokemon.getDisplayName() + " is already max size."));
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

        return true;
    }

    @Override
    public void activate(Pokemon pokemon, Player player) {

        EnumGrowth gr = null;
        switch (pokemon.getGrowth()){
            case Microscopic:
                gr = EnumGrowth.Pygmy;
                break;
            case Pygmy:
                gr = EnumGrowth.Runt;
                break;
            case Runt:
                gr = EnumGrowth.Small;
                break;
            case Small:
                gr = EnumGrowth.Ordinary;
                break;
            case Ordinary:
                gr = EnumGrowth.Huge;
                break;
            case Huge:
                gr = EnumGrowth.Giant;
                break;
            case Giant:
                gr = EnumGrowth.Enormous;
                break;
            case Enormous:
                gr = EnumGrowth.Ginormous;
                break;
            default:
                gr = null;
                break;
        }
        if(gr != null){
            pokemon.setGrowth(gr);
            player.sendMessage(Text.of(TextColors.GREEN, pokemon.getDisplayName() + " is now " + gr.name()));
        }
        else {
            player.sendMessage(Text.of(TextColors.RED, "Something went wrong"));
            player.getInventory().first().offer(this.item);
        }

    }

    @Override
    public List<Text> info() {
        return Arrays.asList(Text.of(TextColors.DARK_PURPLE,"Right-click a Pokemon to make it bigger"),Text.of(TextColors.DARK_GRAY,"token id:" + this.name.name()));

    }
}
