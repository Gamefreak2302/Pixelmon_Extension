package io.gamefreak.pixelmonextension.token.Pixelmontoken;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import io.gamefreak.pixelmonextension.Pixelmonextension;
import io.gamefreak.pixelmonextension.token.Token;
import io.gamefreak.pixelmonextension.token.TokenTypes;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Possible tokens:
 *    - Pokeball token
 * */
public abstract class PixelmonToken implements Token {

    protected String displayName;
    protected TokenTypes.TokenName name;
    protected ItemStack item;


    /**
     * Read blacklist for specific token
     * @return enumspecies with blacklist
     */
    @Override
    public List<EnumSpecies> getBlacklist() {
        List<EnumSpecies> species = new ArrayList<>();
        try {
            ConfigurationNode nodes = Pixelmonextension.INSTANCE.mainConfig.load();
            ConfigurationNode Tokens = nodes.getNode("Tokens");
            ConfigurationNode type = Tokens.getNode(name.name());
            if(!type.getNode("blacklist").isVirtual()){
                for(ConfigurationNode node : type.getNode("blacklist").getChildrenList()){
                    try{
                        EnumSpecies specie = EnumSpecies.getFromNameAnyCase(node.getString());
                        if(specie != null)
                            species.add(specie);
                    }catch(NullPointerException ex){
                        Pixelmonextension.INSTANCE.logger.error("Pokemon not recognized: " + getName() + " - " + node.getString());
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return species;
    }
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
    public abstract List<Text> info();

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
    public ItemStack createItem(ItemType type,int damage,List<Text> lore) {
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
            ItemType itemtype;

            ConfigurationNode nodes = Pixelmonextension.INSTANCE.mainConfig.load();
            ConfigurationNode Tokens = nodes.getNode("Tokens");
            ConfigurationNode type = Tokens.getNode(name.name());

            if (type == null) {
                Pixelmonextension.INSTANCE.sendError("Could not read token data");
                return false;
            }

            if(!type.getNode("name").isVirtual()) {
                String display = type.getNode("name").getString();
                if (display != null && !display.trim().equalsIgnoreCase("")) {
                    displayName = display.trim();
                }
            }

            int damage = 0;
            if(!type.getNode("damage").isVirtual()) {
                if (type.getNode("damage").getValue() != null && type.getNode("damage").getInt() > 0) {
                    damage = type.getNode("damage").getInt();
                }
            }

            if(!type.getNode("id").isVirtual()) {
                String itemid = type.getNode("id").getString();
                if (type.getNode("id") != null && !itemid.trim().equalsIgnoreCase("")) {
                    itemid = itemid.trim();
                    if (Sponge.getRegistry().getType(ItemType.class, itemid).isPresent()) {
                        itemtype = Sponge.getRegistry().getType(ItemType.class, itemid).get();
                    } else {
                        itemtype = ItemTypes.NETHER_STAR;
                        Pixelmonextension.INSTANCE.sendError(itemid + " is not a valid item, setting item netherstar");
                        type.getNode("id").setValue("minecraft:nether_star");
                    }

                } else {
                    itemtype = ItemTypes.NETHER_STAR;
                    Pixelmonextension.INSTANCE.sendError("item id of " + name.name() + " is invalid or missing, setting item netherstar");
                    type.getNode("id").setValue("minecraft:nether_star");
                }
            }else{
                itemtype = ItemTypes.NETHER_STAR;
                Pixelmonextension.INSTANCE.sendError("item id of \"" + name.name() + "\" is missing, setting item netherstar");
                type.getNode("id").setValue("minecraft:nether_star");
            }
            List<Text> lore = new ArrayList<>();
            if(!type.getNode("lore").isVirtual()){
                for(ConfigurationNode node:type.getNode("lore").getChildrenList()){
                    if(!node.getString().trim().equals("")){
                        lore.add(Text.of(TextSerializers.FORMATTING_CODE.deserialize(node.getString())));
                    }
                }
                if(lore.size() == 0){
                    lore = info();
                }else{
                    lore.add(Text.of(TextColors.DARK_GRAY,"token id:" + this.name.name()));
                }
            }
            else {
                lore = info();
            }
            this.item = createItem(itemtype,damage,lore);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

}
