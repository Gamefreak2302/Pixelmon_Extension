package io.gamefreak.pixelmonextension.token.SpawnTokens;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.IStorageManager;
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
import java.util.Collections;
import java.util.List;

public abstract class SpawnTokens implements Token {


    public abstract void activate(Player player);

    protected String displayName;
    protected TokenTypes.TokenName name;
    protected ItemStack item;

    @Override
    public List<EnumSpecies> getBlacklist() {
        List<EnumSpecies> species = new ArrayList<>();
        try {
            ConfigurationNode nodes = Pixelmonextension.INSTANCE.mainConfig.load();
            ConfigurationNode Tokens = nodes.getNode("Tokens");
            ConfigurationNode type = Tokens.getNode(name.name());
            if(!type.getNode("blacklist").isVirtual()){
                for(ConfigurationNode node : type.getNode("blacklist").getChildrenList()){
                    EnumSpecies specie = EnumSpecies.getFromNameAnyCase(node.getString());
                    if(specie == null){
                        Pixelmonextension.INSTANCE.logger.error("Pokemon not recognized: " + getName() + " - " + specie.name);
                    }else{
                        species.add(specie);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return species;
    }

    @Override
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

    @Override
    public TokenTypes.TokenName getName() {
        return name;
    }

    @Override
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

                        .set(DataQuery.of("UnsafeData", "IsSpawnToken"), true)
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

            List<Text> lore = new ArrayList<>();
            if(!type.getNode("Lore").isVirtual()){
                for(ConfigurationNode node:type.getNode("Lore").getChildrenList()){
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

    public Pokemon addPokemonToPlayer(Player player, List<EnumSpecies> list,boolean shiny){
        Collections.shuffle(list);
        EnumSpecies species = list.get(0);
        Pokemon pokemon = Pixelmon.pokemonFactory.create(species);
        pokemon.setShiny(shiny);
        IStorageManager manager = Pixelmon.storageManager;
        if(manager.getParty(player.getUniqueId()).hasSpace()){
            manager.getParty(player.getUniqueId()).add(pokemon);
        }else{
            manager.getPCForPlayer(player.getUniqueId()).add(pokemon);
            player.sendMessage(Text.of(TextColors.GRAY,pokemon.getDisplayName() + " has been added to your pc"));
        }
        return pokemon;
    }
}
