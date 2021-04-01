package io.gamefreak.pixelmonextension.token;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

import io.gamefreak.pixelmonextension.Pixelmonextension;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/*
 * Possible tokens:
 *    - Pokeball token
 * */
public abstract class PixelmonToken {

    protected String displayName;
    protected TokenTypes.TokenName name;
    protected ItemStack item;


    /**
     * Will check if the effect of the effect is available
     * The effect is available when the condition is not met.
     * An example when giving the fly possibility, check if the pokemon can not fly
     *
     * @param player  The player who uses the token
     * @param pokemon The pokemon the token is used on
     * @return true if all valid conditions are good
     */
    public abstract boolean checkValid(Pokemon pokemon, Player player);


    /**
     * It will activate the effect of the token.
     * @param pokemon Pokemon it will be activated on
     * @param player  Player who uses the token
     */
    public abstract void activate(Pokemon pokemon, Player player);

    /**
     * Give information about a token
     * @return information
     */
    public abstract String info();

    /**
     * returns the item of the token, if token is invalid, returns stick with name Bad token
     * @return item of token
     */
    public ItemStack getItem() {
        if (item == null)
            return ItemStack.builder().add(Keys.DISPLAY_NAME, Text.of("&cBad token")).itemType(ItemTypes.STICK).build();
        return item;
    }

    /**
     * get tokenname of token
     * @return token name
     */
    public TokenTypes.TokenName getName() {
        return name;
    }

    /**
     * Get the name of the token
     * @return name for item
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * create the item the player receives when a token is created.
     * The item will be enchanted, given a name and add the right nbt data
     * @param type Itemtype of the token
     * @param damage Metadata of the token (to seperate colors)
     * @return Itemstack
     */
    public ItemStack createItem(ItemType type,int damage) {
        ItemStack stack = ItemStack.builder().itemType(type)
                .add(Keys.DISPLAY_NAME, Text.of(TextSerializers.FORMATTING_CODE.deserialize(displayName)))
                .build();

        stack = ItemStack.builder()
                .fromContainer(stack.toContainer()

                        .set(DataQuery.of("UnsafeData", "IsToken"), true)
                        .set(DataQuery.of("UnsafeData", "TokenType"), name.name())
                        .set(DataQuery.of("UnsafeData","HideFlags"), 1)
                        .set(DataQuery.of("UnsafeDamage"),damage)
                )
                .build();
        Enchantment en = Enchantment.builder().type(EnchantmentTypes.UNBREAKING).level(1).build();
        List<Enchantment> ens = Arrays.asList(en);
        List<Text> lore = Arrays.asList(Text.of(TextColors.DARK_PURPLE,info()),Text.of(TextColors.DARK_PURPLE,"right-click pokemon to use"), Text.of(TextColors.DARK_GRAY,"token id:" + name.name().toLowerCase()));
        stack.offer(Keys.ITEM_ENCHANTMENTS,ens);
        stack.offer(Keys.ITEM_LORE,lore);

        //EnchantmentData enchantmentData = stack.getOrCreate(EnchantmentData.class).get();
        return stack;

    }

    /**
     * reads and changes data following the configs
     * @return false if token data could not be read, otherwise true
     */
    public boolean setInfo() {
        try {
            ItemType itemtype = null;

            ConfigurationNode nodes = Pixelmonextension.INSTANCE.mainConfig.load();
            ConfigurationNode Tokens = nodes.getNode("Tokens");
            ConfigurationNode type = Tokens.getNode(name.name());

            if (type == null) {
                Pixelmonextension.INSTANCE.logger.error("Could not read token data");
                return false;
            }

            if(!type.getNode("name").isVirtual()) {
                String display = type.getNode("name").getString();
                if (display != null && !display.isEmpty()) {
                    displayName = display.trim();
                }
            }

            int damage = 0;
            if(!type.getNode("damage").isVirtual()) {
                if (!type.getNode("damage").isEmpty() && type.getNode("damage").getInt() > 0) {
                    damage = type.getNode("damage").getInt();
                }
            }

            if(!type.getNode("id").isVirtual()) {
                String itemid = type.getNode("id").getString();
                if (type.getNode("id") != null && !itemid.isEmpty()) {
                    itemid = itemid.trim();
                    if (Sponge.getRegistry().getType(ItemType.class, itemid).isPresent()) {
                        itemtype = Sponge.getRegistry().getType(ItemType.class, itemid).get();
                    } else {
                        itemtype = ItemTypes.NETHER_STAR;
                        Pixelmonextension.INSTANCE.logger.error(itemid + " is not a valid item");
                    }

                } else {
                    itemtype = ItemTypes.NETHER_STAR;
                    Pixelmonextension.INSTANCE.logger.error("item id of " + name.name() + " is invalid or missing");
                }
            }else{
                itemtype = ItemTypes.NETHER_STAR;
                Pixelmonextension.INSTANCE.logger.error("item id of \"" + name.name() + "\" is missing");
            }

            this.item = createItem(itemtype,damage);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

}
