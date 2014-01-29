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

Building
--------
If you manage to compile this, you probably could've written it better than I did.
