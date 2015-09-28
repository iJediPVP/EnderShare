/*COMMAND LIST
/eshare create player
/eshare accept player
/eshare disband

* */
package me.i_Jedi.EnderShare.Commands;

import me.i_Jedi.EnderShare.Info.PlayerInfo;
import me.i_Jedi.EnderShare.Info.ShareInfo;
import me.i_Jedi.EnderShare.Timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class EShareCom implements CommandExecutor {
    //Variables
    private JavaPlugin plugin;
    public static HashMap<Player, Integer> cdList = new HashMap<>();
    //Constructor
    public EShareCom(JavaPlugin jp){
        plugin = jp;
    }

    //Command
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        //Check for player
        if(sender instanceof Player){
            //Store player and command
            Player player = (Player) sender;
            String command = cmd.getName().toUpperCase();

            //Check command
            if(command.equals("ESHARE")){
                //Check for args
                if(args.length == 1){ //Check for disband
                    if(args[0].equals("DISBAND")){
                        //Do something
                    }
                }else if(args.length == 2){ //Create or accept
                    String subCmd = args[0].toUpperCase();
                    String pName = args[1];

                    //Make arg[1] is an online player
                    Player sharePartner = null;
                    try{
                        sharePartner = Bukkit.getPlayer(pName);
                        if(!sharePartner.isOnline()){
                            return true;
                        }
                    }catch(NullPointerException npe){
                        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + ChatColor.GOLD + "The player must be online before you can share with them.");
                        return true;
                    }

                    //Make sure the player not interacting with themselves
                    if(player.equals(sharePartner)){
                        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + ChatColor.GOLD + "You cannot share with yourself!");
                        return true;
                    }

                    //Check which sub command
                    //Create
                    if(subCmd.equals("CREATE")){
                        //Make sure the player does not already have a share
                        PlayerInfo pInfo = new PlayerInfo(player, plugin);
                        if(!pInfo.getHasShare()){
                            //Check if the player already has an invite out
                            if(cdList.containsKey(player)){
                                player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + ChatColor.GOLD + "You already have a share request sent. Please wait till it expires before sending another one!");
                                return true;
                            }

                            //Start 30 second cooldown
                            BukkitTask task = new Timer(plugin, player, sharePartner).runTaskTimer(plugin, 0L, 30 * 20L);
                            cdList.remove(player);
                            cdList.put(player, task.getTaskId());
                            //Alert the other player
                            sharePartner.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + sharePartner.getPlayerListName() + ChatColor.GOLD +
                                    " has asked to share Ender Chests with you! You have 30 seconds to accept by using " + ChatColor.RED + "" + ChatColor.BOLD +
                                    "/eshare accept " + player.getName() + ChatColor.GOLD + "!");
                            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] "  + ChatColor.GOLD + "Share request sent to " + sharePartner.getPlayerListName() + ChatColor.GOLD + "!");
                            pInfo.setReqTo(sharePartner.getUniqueId().toString());
                        }else{
                            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] "  + ChatColor.GOLD + "You already have a share. You must disband it before creating a new one!");
                            return true;
                        }

                    //Accept
                    }else if(subCmd.equals("ACCEPT")){
                        //Make sure neither player has a share
                        PlayerInfo pInfo = new PlayerInfo(player, plugin);
                        PlayerInfo spInfo = new PlayerInfo(sharePartner, plugin);

                        //Make sure the requesting player is online
                        if(!pInfo.getHasShare() && !spInfo.getHasShare()){
                            if(!spInfo.getHasShare()){
                                //Make sure share partner has sent a request to this person
                                if(player.getUniqueId().toString().equals(spInfo.getReqTo())) {
                                    spInfo.setReqTo(null);
                                    if (cdList.containsKey(sharePartner)) {
                                        //Cancel cooldown
                                        Bukkit.getScheduler().cancelTask(cdList.get(sharePartner));
                                        cdList.remove(sharePartner);
                                        spInfo.setReqTo(null);
                                    }

                                    //Do sharing stuff
                                    pInfo.setHasShare(true);
                                    pInfo.setShareCreator(sharePartner.getUniqueId().toString());
                                    pInfo.setSharePartner(sharePartner.getUniqueId().toString());
                                    spInfo.setHasShare(true);
                                    spInfo.setShareCreator(sharePartner.getUniqueId().toString());
                                    spInfo.setSharePartner(player.getUniqueId().toString());

                                    //Merge ender chest inventories
                                    ShareInfo shareInfo = new ShareInfo(plugin, player);
                                    shareInfo.createShareFile();

                                    //Tell the players
                                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + ChatColor.GOLD + "You will now share Ender Chests with " + sharePartner.getPlayerListName() + ChatColor.GOLD + "!");
                                    sharePartner.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + player.getPlayerListName() + ChatColor.GOLD + " has accepted your share request! You will now share Ender Chests with them!");

                                //sharePartner did send player a request
                                }else{
                                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + sharePartner.getPlayerListName() + ChatColor.GOLD + " did not send you a share request!");
                                }

                            //Share partner already has a share
                            }else{
                                sharePartner.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] "  + ChatColor.GOLD + "You already have a share. You must disband it before creating a new one!");
                                return true;
                            }

                        //Player already has a share
                        }else{
                            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] "  + ChatColor.GOLD + "You already have a share. You must disband it before creating a new one!");
                        }
                    }
                //Invalid args. Display a custom help message to the user.
                }else{

                }
            }
        }
        return true;
    }
}
