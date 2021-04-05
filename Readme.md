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
Usage: /token give (\<player\>) (\<amount\>)  
Description: Give player a token, if inventory is full (can't add all tokens) , add token to virtual list  
Permission: pixelmonextension.admin.givetoken  
  
### Read token

Usage: /token bal (\<player\>)   
Description: Read unclaimed tokens  
Permissions: pixelmonextension.readtoken.base (own tokens) , pixelmonextension.readtoken.others (others tokens)
  
### Claim token

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

### Extra notes
- Tokens can not be placed 
- Tokens can not handle nbt data yet
- Tokens always have an enchanted look

### Warning
- This plugin is created by Gamefreak_2302 , claiming it to be yours is not allowed. 
- Selling this plugin is not allowed.
- This plugin only requires pixelmon.
- I'm still not the best, bugs can occur (please let me know)
- Token changes don't need /pe reload to be updated 