package io.gamefreak.pixelmonextension;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import io.gamefreak.pixelmonextension.token.PixelmonToken;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;


import java.util.*;

public class DomainController {


    /*
        Storages:
            PixelmontokensUnclaimed
                - Stores the amount of unclaimed pixelmon token a player has
                - Map of players UUID, which contains a map per pixelmon token and his amount
            catchrates
                - Stores updated catchrates for pokemon
                - map of enumspecies and catchrate
     */

    private Map<UUID, Map<TokenTypes.TokenName, Integer>> PixelmontokensUnclaimed;
    private Map<EnumSpecies, Integer> catchrates;


    /**
     * constructor, will initialize lists
     */
    public DomainController(){
        this.PixelmontokensUnclaimed = new HashMap<>();
        this.catchrates = new HashMap<>();
    }


    /**
     * get the list of pixelmon tokens
     * @return return unclaimed pixelmon tokens
     */
    public Map<UUID,Map<TokenTypes.TokenName,Integer>> getPixelmonTokens(){
        return this.PixelmontokensUnclaimed;
    }

    /**
     * Store tokens when inventory is full.
     * @param uuid Player for the tokens
     * @param token Token name of the unclaimed token
     * @param amount Amount of the tokens failed to give
     * @return Return true when token has been added. (can't fail)
     */
    public boolean addUnclaimedToken(UUID uuid, TokenTypes.TokenName token, int amount){
        if(!PixelmontokensUnclaimed.containsKey(uuid)|| PixelmontokensUnclaimed.get(uuid) == null){
            PixelmontokensUnclaimed.put(uuid,new HashMap<>());
        }

        Map<TokenTypes.TokenName,Integer> unclaimedTokens = PixelmontokensUnclaimed.get(uuid);
        if(unclaimedTokens.isEmpty()){

            unclaimedTokens.put(token,amount);
            PixelmontokensUnclaimed.put(uuid,unclaimedTokens);
            return true;
        }

            if(unclaimedTokens.containsKey(token)){
                unclaimedTokens.put(token, unclaimedTokens.get(token) + amount);
                return true;
            }


        unclaimedTokens.put(token,amount);
        return true;
    }

    /**
     * get the unclaimed tokens a player has
     * @param player UUID of a player/user
     * @return list of unclaimed tokens and their amounts
     */
    public Map<TokenTypes.TokenName, Integer> getUnclaimedTokens(UUID player){

        if(PixelmontokensUnclaimed.get(player) == null || PixelmontokensUnclaimed.get(player).isEmpty()){
            return new HashMap<>();
        }

        return PixelmontokensUnclaimed.get(player);
    }

    /**
     * Add unclaimed tokens to the map for unclaimed tokens.
     * If the UUID is unknown, it will receive an entry.
     * @param uuid player UUID
     * @param name Token name
     * @param amount Amount to be added
     */
    public void addToken(UUID uuid, TokenTypes.TokenName name, int amount){
        if(this.PixelmontokensUnclaimed == null){
            this.PixelmontokensUnclaimed = new HashMap<>();
        }
        if(PixelmontokensUnclaimed.get(uuid)==null){
            Map<TokenTypes.TokenName,Integer> token = new HashMap<>();
            token.put(name,amount);
            PixelmontokensUnclaimed.put(uuid,token);
        }
        else{
            Map<TokenTypes.TokenName, Integer> tokens = getUnclaimedTokens(uuid);
            tokens.put(name,amount);
        }

    }

    /**
     * Claims all token of the given UUID
     * @param uuid UUID of player
     */
    public boolean claimToken(UUID uuid, TokenTypes.TokenName name, int amount){

        User user =	Sponge.getServiceManager().provide(UserStorageService.class).get().get(uuid).get();
        Map<TokenTypes.TokenName, Integer> unclaimedTokens = PixelmontokensUnclaimed.get(uuid);

        if(user.isOnline()) {

            Player player = user.getPlayer().get();

            if (unclaimedTokens == null) {
                unclaimedTokens = new HashMap<>();
            }
            if (unclaimedTokens.isEmpty() || unclaimedTokens.get(name) == null ||unclaimedTokens.get(name) == 0) {
                player.sendMessage(Text.of(TextColors.RED, "You don't have enough " + name +" tokens"));
                return false;
            } else {

                if(amount > unclaimedTokens.get(name)){
                    player.sendMessage(Text.of(TextColors.RED,"You don't have that amount of unclaimed tokens"));
                    return false;
                }
                PixelmonToken token = TokenTypes.getTokenFromTokenName(name);
                ItemStack stack = ItemStack.builder().from(token.getItem()).quantity(amount).build();
                if(!player.getInventory().first().canFit(stack)){
                    player.sendMessage(Text.of(TextColors.RED,"Not enough inventory space to take that amount of tokens"));
                    return false;
                }

            }
        }
        unclaimedTokens.put(name, unclaimedTokens.get(name) - amount);
        PixelmontokensUnclaimed.put(uuid,unclaimedTokens);
        return true;
    }

    /**
     * Add/replaces a change to the catchrate of a given pokemon.
     * Insert catchrate to database file
     * @param species
     * @param rate
     */
    public void addCatchrate(EnumSpecies species, int rate){
        if(catchrates == null){
            catchrates = new HashMap<>();
        }
        if(catchrates.containsKey(species)){
            catchrates.remove(species);
        }
        Pixelmonextension.INSTANCE.logger.info("Species = " + species.name + " Rate = " + rate);
        species.getBaseStats().catchRate = rate;
        catchrates.put(species,rate);
    }

    /**
     * return map with pokemon and their catchrtes
     * @return catchrates
     */
    public Map<EnumSpecies,Integer> getCatchrates(){

        return this.catchrates;
    }

    /**
     * Empty lists catchrates and pixelmonTokensUnclaimed (for reload)
     */
    public void clearLists(){
        catchrates = new HashMap<>();
        PixelmontokensUnclaimed = new HashMap<>();
    }


}
