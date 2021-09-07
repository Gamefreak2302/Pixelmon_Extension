package io.gamefreak.pixelmonextension.token.Pixelmontoken;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.List;

public class FriendToken extends PixelmonToken{

    public FriendToken(){
        this.displayName = "&aFriend token";
        this.name = TokenTypes.TokenName.friend;
        this.setInfo();
    }

    @Override
    public boolean checkValid(Pokemon pokemon, Player player) {

        if(pokemon.getOwnerPlayerUUID() != player.getUniqueId()){
            player.sendMessage(Text.of(TextColors.RED,"This pokemon is not yours."));
            return false;
        }

        if(pokemon.getFriendship() >=220){
            player.sendMessage(Text.of(TextColors.RED,"This pokemon already has 220 or more happiness."));
            return false;

        }

        return true;
    }

    @Override
    public void activate(Pokemon pokemon, Player player) {

        pokemon.setFriendship(220);
        player.sendMessage(Text.of(TextColors.GREEN,"Your pokemon now has 220 happiness points"));
    }

    @Override
    public List<Text> info() {
        return Arrays.asList(Text.of(TextColors.DARK_PURPLE,"Right-click a Pokemon to increase the happiness to 220"),Text.of(TextColors.DARK_GRAY,"token id:" + this.name.name()));
    }
}
