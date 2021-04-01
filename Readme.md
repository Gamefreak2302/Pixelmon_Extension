# Pixelmon Extension

# What is it?
Pixelmon extension is a plugin (in building progress) created by Gamefreak_2302.
It's purpose is to extend certain aspects of pixelmon with certain features it's been missing. 

So far this plugin contains: 
- Tokens: Change pokemon stats by right clicking with item
- Catchrates: Change catchrate of pokemons 

Planned additions: Make tokens have a virtual storage (add tokens to /pe readtoken <token> ) 

## Commands

### Give token 
Usage: /pe givetoken (\<player\>) (\<amount\>)  
Description: Give player a token, if inventory is full (can't add all tokens) , add token to virtual list  
Permission: pixelmonextension.admin.givetoken  
  
### Read token

Usage: /pe readtoken (\<player\>)   
Description: Read unclaimed tokens  
Permissions: pixelmonextension.readtoken.base (own tokens) , pixelmonextension.readtoken.others (others tokens)  
  
### Info

Usage: /pe info \<Token\>  
Description: Gives information about a given token  
Permission: pixelmonextension.info  
  
### Claim token

Usage: /pe claimtoken \<Token\>  
Description: Claims a token from your unclaimed tokens  
Permission: pixelmonextension.claimtoken
  
### Change catchrate

Usage: /pe changecatchrate \<Pokemon\> \<catchrate\>  
Description: changes a catchrate of a pokemon  
Permission: pixelmonextension.admin.changecatchrate  
  
### Read catchrate

Usage: /pe readcatchrate   
Description: Get a list of pokemon and their updated catchrates (updated by this plugin)  
Permission: pixelmonextension.admin.readcatchrate  
  
### Reload

Usage: /pe reload  
Description: reload config (tokens will be updated automatically)  
Permission: pixelmonextension.admin.reload  
