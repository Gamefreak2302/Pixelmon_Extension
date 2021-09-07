package io.gamefreak.pixelmonextension.token;

import io.gamefreak.pixelmonextension.token.Pixelmontoken.*;
import io.gamefreak.pixelmonextension.token.SpawnTokens.*;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.item.inventory.ItemStack;

public class TokenTypes {


    /**
     * get an array of the token names
     * @return array of token names
     */
    public static TokenName[] getTokenNames(){
        return TokenName.values();
    }

    /**
     * get token name (enum) from string, not case sensitive
     * @param name
     * @return
     */
    public static TokenName getTokenNameFromString(String name){

        for (TokenName tn : getTokenNames()){
            if(tn.name().equalsIgnoreCase(name)){
                return tn;
            }
        }

        return null;
    }

    public static boolean isToken(ItemStack stack){


        //check spawn token true
        if((stack.toContainer().get(DataQuery.of("UnsafeData", "IsSpawnToken")).isPresent()
        &&  stack.toContainer().getBoolean(DataQuery.of("UnsafeData", "IsSpawnToken")).get())
        //check pixelmon token
        || (stack.toContainer().get(DataQuery.of("UnsafeData", "IsToken")).isPresent() &&
                 stack.toContainer().getBoolean(DataQuery.of("UnsafeData", "IsToken")).get())
        ){
            // check if tokentype is present and is valid
            if(stack.toContainer().get(DataQuery.of("UnsafeData", "TokenType")).isPresent()
                && getTokenNameFromString(stack.toContainer().getString(DataQuery.of("UnsafeData", "TokenType")).get()) != null)
            {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     *
     * @param name Factory pattern for pixelmontoken
     * @return token which matches name
     */
    public static Token getTokenFromTokenName(TokenName name){

        switch (name){
            case Shiny:
                return new ShinyToken();
            case Gender:
                return new GenderToken();
            case SizeUp:
                return new SizeUpToken();
            case SizeDown:
                return new SizeDownToken();
            case MaxHPIvs:
                return new HpIVToken();
            case MaxDefIvs:
                return new DefIvToken();
            case MaxAtkIvs:
                return new AtkIvToken();
            case MaxSpAtkIvs:
                return new SpAtkIvToken();
            case MaxSpDefIvs:
                return new SpDefIvToken();
            case MaxSpeedIvs:
                return new SpeedIvToken();
            case Nature:
                return new NatureToken();
            case Ability:
                return new AbilityToken();
            case HiddenAbility:
                return new HiddenAbilityToken();
            case Pokeball:
                return new PokeballToken();
            case Form:
                return new FormToken();
            case RandomSize:
                return new RandomSizeToken();
            case randomUB:
                return new RandomUbToken();
            case randomShiny:
                return new RandomShinyToken();
            case randomLegend:
                return new RandomLegendToken();
            case randomNonSpecial:
                return new RegularPokemonToken();
            case randomShinyNonSpecial:
                return new RandomShinyRegularToken();
            case randomPokemon:
                return new RandomPokemonToken();

            case MaxAtkEvs:
                return new AtkEvToken();
            case MaxHPEvs:
                return new HpEVToken();
            case MaxDefEvs:
                return new DefEvToken();
            case Normalize:
                return new UnShinyToken();
            case MaxSpAtkEvs:
                return new SpAtkEvToken();
            case MaxSpDefEvs:
                return new SpDefEvToken();
            case MaxSpeedEvs:
                return new SpeedEvToken();
            case ResetEvs:
                return new EvsResetToken();
            case friend:
                return new FriendToken();
            default:
                return null;
        }

    }




    public enum TokenName {

        // modify tokens
        Shiny,SizeUp,SizeDown,Gender,MaxHPIvs,MaxAtkIvs,MaxDefIvs,MaxSpAtkIvs,MaxSpDefIvs,MaxSpeedIvs
        ,Nature,Ability,HiddenAbility,Pokeball, Form,RandomSize,Normalize
        , MaxAtkEvs, MaxDefEvs, MaxHPEvs, MaxSpAtkEvs, MaxSpDefEvs, MaxSpeedEvs, randomShinyNonSpecial
        ,ResetEvs
        //Spawn tokens
        ,randomPokemon,randomShiny,randomLegend,randomUB, friend, randomNonSpecial
    }
}
