package io.gamefreak.pixelmonextension;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import io.gamefreak.pixelmonextension.commands.CommandRegistry;
import io.gamefreak.pixelmonextension.storage.Database;
import io.gamefreak.pixelmonextension.token.PixelmonToken;
import io.gamefreak.pixelmonextension.token.TokenConfigSettings;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.io.IOException;
import java.nio.file.Path;

import static io.gamefreak.pixelmonextension.ServerStats.*;


@Plugin(
        id = ID,
        name = NAME,
        description = DESCRIPTION,
        authors = {
                AUTHOR
        }
)
public class Pixelmonextension {


    @Inject
    public Logger logger;
    @Inject
    public PluginContainer pC;
    public static Pixelmonextension INSTANCE;
    public static DomainController registry;
    public static Database db;

    @Inject
    @ConfigDir(sharedRoot = false)
    public Path configDir;

    public ConfigurationLoader<CommentedConfigurationNode> mainConfig;

    public static Path dbMigrationPath;


    /**
     * When server is starting, create configs and database
     * @param event game initialization
     */
    @Listener
    public void OnServerInitialization(GameInitializationEvent event) {

        INSTANCE = this;
        CommandRegistry.register();
        registry = new DomainController();
        dbMigrationPath = configDir.resolve("data.mv.db");
        db = new Database();


        if (Sponge.getAssetManager().getAsset(pC, "main.conf").isPresent()) {
            logger.info("main.conf is present");
            try {
                Sponge.getAssetManager().getAsset(pC, "main.conf").get().copyToFile(configDir.resolve("main.conf"));

            } catch (IOException e) {
                logger.error("Could not create file");
                e.printStackTrace();
            }
        }

        mainConfig = HoconConfigurationLoader.builder().setPath(configDir.resolve("main.conf")).build();


    }

    /**
     * After server is started, read values of database and set token config
     * @param e game started server
     */
    @Listener
    public void onGameInitializedEvent(GameStartedServerEvent e){
        db.ReadTokens();
        db.ReadCatchrates();
        new TokenConfigSettings();
    }

    /**
     * clear catchrate list, token lists and
     * refills them.
     * Updates token config settings
     */
    public void reload(){

        registry.clearLists();

        db.ReadTokens();
        db.ReadCatchrates();
        new TokenConfigSettings();
    }



    /**
     * Checks if item in hand is token and activates it
     *
     * @param e interact entity event
     */
    @Listener
    public void onClickOnPixelmon(InteractEntityEvent.Secondary.MainHand e) {

        Player player;
        Pokemon pokemon;
        if (e.getSource() instanceof Player) {
            player = (Player) e.getSource();
            if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
                ItemStack stack = player.getItemInHand(HandTypes.MAIN_HAND).get();
                if ((boolean) stack.toContainer().get(DataQuery.of("UnsafeData", "IsToken")).orElse(false)) {
                    if (stack.toContainer().get(DataQuery.of("UnsafeData", "TokenType")).isPresent()) {
                        String tokenType = (String) stack.toContainer().get(DataQuery.of("UnsafeData", "TokenType")).get();
                        PixelmonToken token = TokenTypes.getTokenFromTokenName(TokenTypes.getTokenNameFromString(tokenType));

                        if (e.getTargetEntity() instanceof EntityPixelmon && token != null) {
                            pokemon = ((EntityPixelmon) e.getTargetEntity()).getStoragePokemonData();
                            if (token.checkValid(pokemon, player)) {
                                player.getItemInHand(HandTypes.MAIN_HAND).get().setQuantity(player.getItemInHand(HandTypes.MAIN_HAND).get().getQuantity() - 1);
                                token.activate(pokemon, player);
                                db.UpdatePixelmonTokenOfUuid(player.getUniqueId());
                            }//end token valid
                        } // end check entity
                    }// end check tokentype
                }//end check isToken
            }//end check item in hand
        } //end player
    }

    @Listener
    public void onBlockPlaced(ChangeBlockEvent.Place e) {
        if (e.getSource() instanceof Player) {
            //prevent token from being placed (by accident)
            ItemStackSnapshot item = e.getCause().getContext().get(EventContextKeys.USED_ITEM).get();
            if (item.toContainer().get(DataQuery.of("UnsafeData", "IsToken")).isPresent() && (boolean) item.toContainer().get(DataQuery.of("UnsafeData", "IsToken")).get()) {
                e.setCancelled(true);
            }

        }
    }



}
