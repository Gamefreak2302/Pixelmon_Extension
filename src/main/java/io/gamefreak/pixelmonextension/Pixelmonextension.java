package io.gamefreak.pixelmonextension;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import io.gamefreak.pixelmonextension.commands.CommandRegistry;
import io.gamefreak.pixelmonextension.storage.Database;
import io.gamefreak.pixelmonextension.token.Pixelmontoken.PixelmonToken;
import io.gamefreak.pixelmonextension.token.Pixelmontoken.TokenConfigSettings;
import io.gamefreak.pixelmonextension.token.SpawnTokens.SpawnTokenLists;
import io.gamefreak.pixelmonextension.token.SpawnTokens.SpawnTokens;
import io.gamefreak.pixelmonextension.token.TokenTypes;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.gamefreak.pixelmonextension.ServerStats.*;


@Plugin(
        id = ID,
        name = NAME,
        description = DESCRIPTION,
        authors = {
                AUTHOR
        },
        version = VERSION
)
public class Pixelmonextension {


    @Inject
    public Logger logger;
    @Inject
    public PluginContainer pC;
    public static Pixelmonextension INSTANCE;
    public static Registry registry;
    public static Database db;

    @Inject
    @ConfigDir(sharedRoot = false)
    public Path configDir;

    public ConfigurationLoader<CommentedConfigurationNode> mainConfig;
    public ConfigurationLoader<CommentedConfigurationNode> databaseConfig;

    public static Path dbMigrationPath;


    /**
     * When server is starting, create configs and database
     * @param event game initialization
     */
    @Listener
    public void OnServerInitialization(GameInitializationEvent event) {

        INSTANCE = this;
        CommandRegistry.register();
        registry = new Registry();
        dbMigrationPath = configDir.resolve("data.mv.db");
        db = new Database();

        if (!Files.exists(configDir.resolve("main.conf"))) {
                //Sponge.getAssetManager().getAsset(pC, "main.conf").get().copyToFile(configDir.resolve("main.conf"));
                pC.getAsset("main.conf").ifPresent(asset -> {
                    try {
                        //configDir.resolve("main.conf");
                        asset.copyToDirectory(configDir);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                });

        }

        if (!Files.exists(configDir.resolve("database.conf"))) {
            //Sponge.getAssetManager().getAsset(pC, "main.conf").get().copyToFile(configDir.resolve("main.conf"));
            pC.getAsset("database.conf").ifPresent(asset -> {
                try {
                    //configDir.resolve("main.conf");
                    asset.copyToDirectory(configDir);
                } catch (IOException e) {

                    e.printStackTrace();
                }
            });

        }
        mainConfig = HoconConfigurationLoader.builder().setPath(configDir.resolve("main.conf")).build();
        databaseConfig = HoconConfigurationLoader.builder().setPath(configDir.resolve("database.conf")).build();



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
        new SpawnTokenLists();
        this.logger.info(" ====== " + NAME + " " + VERSION + " has started successfully ====== ");
    }

    /**
     * clear catchrate list, token lists and
     * refills them.
     * Updates token config settings
     */
    public void reload(){

        registry.clearLists();
        db = new Database();
        db.ReadTokens();
        db.ReadCatchrates();
        new TokenConfigSettings();
        new SpawnTokenLists();
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
                        PixelmonToken token =(PixelmonToken)TokenTypes.getTokenFromTokenName(TokenTypes.getTokenNameFromString(tokenType));

                        if (e.getTargetEntity() instanceof EntityPixelmon && token != null) {
                            pokemon = ((EntityPixelmon) e.getTargetEntity()).getStoragePokemonData();
                            if (token.checkValid(pokemon, player)) {
                                player.getItemInHand(HandTypes.MAIN_HAND).get().setQuantity(player.getItemInHand(HandTypes.MAIN_HAND).get().getQuantity() - 1);
                                token.activate(pokemon, player);
                                logger.info(player + " has used a " + token.getDisplayName());
                            }//end token valid

                        } // end check entity
                    }// end check tokentype
                }//end check isToken

            }//end check item in hand
        } //end player
    }

    @Listener
    public void OnRightCLick(InteractItemEvent.Secondary.MainHand e){

        if(e.getSource() instanceof Player){
            Player player = (Player) e.getSource();
            ItemStack stack = e.getItemStack().createStack();
            if((boolean) stack.toContainer().get(DataQuery.of("UnsafeData", "IsSpawnToken")).orElse(false)) {
                if (stack.toContainer().get(DataQuery.of("UnsafeData", "TokenType")).isPresent()) {
                    String tokenType = (String) stack.toContainer().get(DataQuery.of("UnsafeData", "TokenType")).get();
                    SpawnTokens token = (SpawnTokens) TokenTypes.getTokenFromTokenName(TokenTypes.getTokenNameFromString(tokenType));
                    player.getItemInHand(HandTypes.MAIN_HAND).get().setQuantity(player.getItemInHand(HandTypes.MAIN_HAND).get().getQuantity() - 1);
                    token.activate(player);
                    logger.info(player.getName() + " has used a " + token.getDisplayName());
                }//End tokentype is present
            }// end is spawn token
        }
    }

    @Listener
    public void onBlockPlaced(ChangeBlockEvent.Place e) {
        if (e.getSource() instanceof Player) {
            //prevent token from being placed (by accident)
            if(e.getCause().getContext().get(EventContextKeys.USED_ITEM).isPresent()) {
                ItemStackSnapshot item = e.getCause().getContext().get(EventContextKeys.USED_ITEM).get();
                if (item.toContainer().get(DataQuery.of("UnsafeData", "IsToken")).isPresent() && (boolean) item.toContainer().get(DataQuery.of("UnsafeData", "IsToken")).get()) {
                    e.setCancelled(true);
                }

                if (item.toContainer().get(DataQuery.of("UnsafeData", "IsSpawnToken")).isPresent() && (boolean) item.toContainer().get(DataQuery.of("UnsafeData", "IsSpawnToken")).get()) {
                    e.setCancelled(true);
                }

            }

        }
    }

    public void sendError(String error){
        if(error == null || error.trim().equalsIgnoreCase("")){
            return;
        }else{
            logger.error(String.format(
                    "\n========= Pixelmon extension Error Below=========\n" +
                            "\n" +
                            "%s\n" +
                            "\n" +
                            "========= Pixelmon extension Error Above =========\n", error
            ));
        }

    }

}
