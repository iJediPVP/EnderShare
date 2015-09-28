# EnderShare
**This plugin allows players to share Ender Chests.**

**Commands**
* /eshare
  * Displays a list of a commands.
  * Usage: **/eshare**
* /eshare create
  * Create an Ender Share with another player.
  * Usage: **/eshare create <player>**
* /eshare accept
  * Accept an Ender Share request.
  * Usage: **/eshare accept <player>**
* /eshare disband
  * Disband an Ender Share. This can only be done if:
    * The command sender has an Ender Share with someone.
    * Both players involved in the Ender Share are online.
    * The Ender Share inventory is empty. (To prevent the loss of items)
  * Usage: **/eshare disband**

**Permissions**
* enderShare.*:
  * Give access to all of the EnderShare commands.
* enderShare.eShare:
  * Give access to the **/eshare** command.
* enderShare.eShare.create:
  * Give access to the **/eshare create** command
* enderShare.eShare.accept:
  * Give access to the **/eshare accept** command
* enderShare.eShare.disband:
  * Give access to the **/eshare disband** command
