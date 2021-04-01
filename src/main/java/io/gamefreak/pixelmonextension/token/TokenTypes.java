package io.gamefreak.pixelmonextension.token;

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

    /**
     *
     * @param name Factory pattern for pixelmontoken
     * @return token which matches name
     */
    public static PixelmonToken getTokenFromTokenName(TokenName name){

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
            default:
                return null;
        }

    }




    public enum TokenName {

        Shiny,SizeUp,SizeDown,Gender,MaxHPIvs,MaxAtkIvs,MaxDefIvs,MaxSpAtkIvs,MaxSpDefIvs,MaxSpeedIvs
        ,Nature,Ability,HiddenAbility,Pokeball, Form,RandomSize
    }
}
