package io.gamefreak.pixelmonextension.token.Pixelmontoken;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import io.gamefreak.pixelmonextension.Pixelmonextension;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokenConfigSettings {

    private static List<EnumSpecies> blacklist;
    private static boolean allowLegendaryModification;
    private static boolean allowUbModification;

    public TokenConfigSettings() {


        try {
            CommentedConfigurationNode nodes = Pixelmonextension.INSTANCE.mainConfig.load();

            this.blacklist = new ArrayList<>();
            if (!nodes.getNode("blacklisted").isVirtual()) {
                for (CommentedConfigurationNode node : nodes.getNode("blacklisted").getChildrenList()) {
                    EnumSpecies specie = EnumSpecies.getFromNameAnyCaseNoTranslate(node.getString());
                    if (specie != null) {
                        blacklist.add(specie);
                    } else {
                        Pixelmonextension.INSTANCE.logger.error(node.getString() + " is not a valid pokemon, make sure the name is english and written correctly (no special symbols (dots, quotes ... ), nidoran = nidoranmale/nidoranfemale). ");
                    }
                }
            }

            if (!nodes.getNode("allowLegendaryModification").isVirtual()) {
                allowLegendaryModification = nodes.getNode("allowLegendaryModification").getBoolean();

            } else {
                allowLegendaryModification = false;
            }
            if (!nodes.getNode("allowLegendaryModification").isVirtual()) {
                allowUbModification = nodes.getNode("allowUBModification").getBoolean();

            } else {
                allowUbModification = false;
            }

        } catch (IOException e) {
            Pixelmonextension.INSTANCE.logger.error("Could not read the main config");
        }
    }

    public static boolean allowModification(EnumSpecies species) {

        if (blacklist.contains(species)) {
            return false;
        }

        if (species.isLegendary() && !allowLegendaryModification) {
            return false;
        }

        if (species.isUltraBeast() && !allowUbModification) {
            return false;
        }

        return true;
    }

}
