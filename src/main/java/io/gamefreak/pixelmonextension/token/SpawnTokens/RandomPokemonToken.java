package io.gamefreak.pixelmonextension.token.SpawnTokens;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.List;

public class RandomPokemonToken extends SpawnTokens{

    public RandomPokemonToken(){
        this.name = TokenTypes.TokenName.randomPokemon;
        this.displayName = "&aRandom pokemon token";
        setInfo();
    }

    @Override
    public void activate(Player player) {

        List<EnumSpecies> list = SpawnTokenLists.getAllPokemonList();
        Pokemon pokemon = this.addPokemonToPlayer(player,list,false);
        player.sendMessage(Text.of(TextColors.GREEN, "You just received " + pokemon.getDisplayName()));
    }

    @Override
    public List<Text> info() {
        return Arrays.asList(
                Text.of(TextColors.DARK_PURPLE,"Right-click item to receive a random pokemon"),
                Text.of(TextColors.DARK_GRAY,"token id:" + this.name.name() )
        );
    }
}
