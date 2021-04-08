package io.gamefreak.pixelmonextension.token;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import java.util.List;

public interface Token {




    /**
     * Give information about a token
     * @return information
     */
    List<Text> info();

    /**
     * returns the item of the token, if token is invalid, returns stick with name Bad token
     * @return item of token
     */
    ItemStack getItem();

    /**
     * get tokenname of token
     * @return token name
     */
    TokenTypes.TokenName getName();

    /**
     * Get the name of the token
     * @return name for item
     */
    String getDisplayName();

    /**
     * create the item the player receives when a token is created.
     * The item will be enchanted, given a name and add the right nbt data
     * @param type Itemtype of the token
     * @param damage Metadata of the token (to seperate colors)
     * @return Itemstack
     */
    public ItemStack createItem(ItemType type, int damage);

    /**
     * reads and changes data following the configs
     * @return false if token data could not be read, otherwise true
     */
    boolean setInfo();



}
