PlayerHider
===========

**PlayerHider** is a plugin that shows players further than a configurable distance as sneaking to make underground bases harder to spot. It also features Line of Sight based hiding of players.

This is done via Packet Manipulation through ProtocolLib which shouldn't trigger any Anti-Cheating Measures or interfere with other plugins.

It uses code from Sneaky:
Sneaky (C) 2013 Kristian S. Stangeland, Licensed under the GNU GPL2, available at http://www.comphenix.net/Sneaky or http://dev.bukkit.org/server-mods/sneaky/
It also uses Plugin-Metrics by Hidendra, available here: https://github.com/Hidendra/Plugin-Metrics

Dependencies
------------
This plugin requires [ProtocolLib](https://github.com/aadnk/ProtocolLib) to function! 


Features
--------
* Shows players further than the specified distance away from a player as sneaking to that player
* If LoS is turned on, shows players within that distance as sneaking if they are behind objects
* Can be hidden for operators, admins, moderators...

Commands
--------
There are no commands, currently.

Permissions
-----------
<table>
  <tr>
    <th>Permission</th>
    <th>Description</th>
    <th>Default</th>
  </tr>
  <tr>
    <td>playerhider.hide.autosneak</td>
    <td>Whether or not to hide all automatic sneaking for this player.</td>
    <td>op</td>
  </tr>
</table>

Config
------
Very easy to configure, disable and reeanble the plugin for changes to take effect:
* updatecooldown: Cooldown in ticks. Higher cooldown means better performance but slower updates. 
* sneakdistance: Players further apart than this distance (in blocks) are shown as sneaking to each other. If LoS is active players who are within that distance from each other are checked for visibility and are shown as sneaking if they are not visible to each other. You can set this pretty high, but this will increase server load if many players are within the distance from each other. This should be a negligible on any servers maintaining the deafult tickrate but might be a concern on weaker servers.
* LoS: This turns Line of Sight hiding on or off, default off, set true to turn it on. 
* combattag: Turns CombatTag integration on or off. If on, a tagged player is not automatically hidden.
