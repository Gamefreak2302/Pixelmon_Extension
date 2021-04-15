# Pixelmon Extension

# What is it?
A token plugin for pixelmon. Tokens which either changes a pokemon or give a pokemon.  
Apart from tokens, there's also a possibility to change catchrates of pokemon with a single command.  

So far this plugin contains: 
- Tokens: Change pokemons stats and looks
- Tokens: Give pokemon on click with token
- Catchrates: Change catchrate of pokemons 


Planned additions:

- Make external database possible  
- Add blacklist different from each token (like necrozma immune for form token)
- Better error handler ( make errors more visible )
- Make lore customizable ( the id will stay )  
- Translations ( english - dutch ) ?? ( not determined yet )
## Commands

### Token give 
Usage: /token give (\<player\>) (\<amount\>)  
Description: Give player a token, if inventory is full (can't add all tokens) , add token to virtual list  
Permission: pixelmonextension.admin.givetoken  
  
### Token bal

Usage: /token balance (\<player\>)  
Alias: bal,balance,list  
Description: Read unclaimed tokens  
Permissions: pixelmonextension.readtoken.base (own tokens) , pixelmonextension.readtoken.others (others tokens)
  
### Token Claim

Usage: /token claim \<Token\>  
Description: Claims a token from your unclaimed tokens  
Permission: pixelmonextension.claimtoken
  
### Change catchrate

Usage: /token changecatchrate \<Pokemon\> \<catchrate\>  
Description: changes a catchrate of a pokemon  
Permission: pixelmonextension.admin.changecatchrate  
  
### Read catchrate

Usage: /token readcatchrate   
Description: Get a list of pokemon and their updated catchrates (updated by this plugin)  
Permission: pixelmonextension.admin.readcatchrate  
  
### Reload

Usage: /token reload  
Description: reload config (tokens will be updated automatically)  
Permission: pixelmonextension.admin.reload  

### Convert
Usage: /token convert (< Tokenname >) (< amount >)  
Description: Convert tokens from physical to virtual form and from virtual to physical  
Permission: pixelmonextension.convert  

### Extra notes
- Tokens can not be placed 
- Tokens can not handle nbt data yet
- Tokens always have an enchanted look  
- On clicking a token in token balance, converts 1 token
- Token changes don't need /token reload to be updated
### Warning
- This plugin is created by Gamefreak_2302 , claiming it to be yours is not allowed. 
- Selling this plugin is not allowed without permission.
- This plugin only requires pixelmon.
- I'm still not the best, bugs can occur (please let me know)
