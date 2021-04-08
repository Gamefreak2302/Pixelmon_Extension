package io.gamefreak.pixelmonextension.token.SpawnTokens;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import io.gamefreak.pixelmonextension.Pixelmonextension;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpawnTokenLists {

    private static List<EnumSpecies> UBList,LegendList, RegularList,AllPokemonList,Blacklist;

    public SpawnTokenLists(){

        UBList = new ArrayList<>();
        LegendList = new ArrayList<>();
        RegularList = new ArrayList<>();
        Blacklist = new ArrayList<>();
        AllPokemonList = new ArrayList<>();

        try {
            CommentedConfigurationNode nodes = Pixelmonextension.INSTANCE.mainConfig.load();
            if(!nodes.getNode("blacklistedSpawns").isVirtual()){
                for(CommentedConfigurationNode node : nodes.getNode("blacklistedSpawns").getChildrenList()){
                    EnumSpecies species = EnumSpecies.getFromNameAnyCase(node.getString());
                    if(species != null){
                        Blacklist.add(species);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Blacklist.add(EnumSpecies.MissingNo);

        for(EnumSpecies species: EnumSpecies.values()){

            if(!Blacklist.contains(species)){
                if(species.isUltraBeast()){
                    UBList.add(species);
                }
                if(species.isLegendary()){
                    LegendList.add(species);
                }
                if(!species.isLegendary() && !species.isUltraBeast()){
                    RegularList.add(species);
                }
                AllPokemonList.add(species);
            }

        }
    }

    public static List<EnumSpecies> getLegendList() {
        return LegendList;
    }

    public static List<EnumSpecies> getRegularList() {
        return RegularList;
    }

    public static List<EnumSpecies> getUBList() {
        return UBList;
    }

    public static List<EnumSpecies> getAllPokemonList() {
        return AllPokemonList;
    }
    //        ,randomPokemon,randomShiny,randomLegend,randomUB,randomNonSpecial,randomShinyNonSpecial

}
