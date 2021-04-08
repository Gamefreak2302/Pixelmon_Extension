package io.gamefreak.pixelmonextension.token.SpawnTokens;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.IStorageManager;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import io.gamefreak.pixelmonextension.Pixelmonextension;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RandomUbToken extends SpawnTokens{


    public RandomUbToken(){

        this.name = TokenTypes.TokenName.randomUB;
        this.displayName = "&aUltra beast token";
        setInfo();
    }
    @Override
    public void activate(Player player) {

        List<EnumSpecies> list = SpawnTokenLists.getUBList();
        Pokemon pokemon = this.addPokemonToPlayer(player,list,false);
        player.sendMessage(Text.of(TextColors.GREEN, "You just received " + pokemon.getDisplayName()));
    }

    @Override
    public List<Text> info() {
        return Arrays.asList(
                Text.of(TextColors.DARK_PURPLE,"Right-click item to give yourself a random ultra beast"),
                Text.of(TextColors.DARK_GRAY,"token id:" + this.name.name() )
        );
    }
}
