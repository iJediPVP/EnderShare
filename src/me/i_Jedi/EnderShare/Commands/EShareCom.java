/*COMMAND LIST
/eshare create player
/eshare accept player
/eshare disband

* */
package me.i_Jedi.EnderShare.Commands;

import me.i_Jedi.EnderShare.Info.PlayerInfo;
import me.i_Jedi.EnderShare.Info.ShareInfo;
import me.i_Jedi.EnderShare.Main;
import me.i_Jedi.EnderShare.Timers.DisbandTimer;
import me.i_Jedi.EnderShare.Timers.ShareTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EShareCom implements CommandExecutor {
    //Variables
    private JavaPlugin plugin;
    public static HashMap<Player, Integer> shareCDList = new HashMap<>();
    public static HashMap<Player, Integer> disbandCDList = new HashMap<>();

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
                //Check for perms
                if(player.hasPermission("enderShare.eShare")){
                    //Check for args
                    if(args.length == 1){ //Check for disband
                        if(args[0].toUpperCase().equals("DISBAND")){
                            //Check for perms
                            if(player.hasPermission("enderShare.eShare.disband")){
                                //Check if the player has a share
                                PlayerInfo pInfo = new PlayerInfo(player, plugin);
                                if(pInfo.getHasShare()){
                                    //Check if the other player is online
                                    Player sharePartner;
                                    try{
                                        sharePartner = Bukkit.getPlayer(UUID.fromString(pInfo.getSharePartner()));
                                        if(!sharePartner.isOnline()){
                                            return true;
                                        }
                                    }catch(NullPointerException npe){
                                        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + ChatColor.GOLD + "Your share partner must be online before you can disband your share with them.");
                                        return true;
                                    }

                                    //Check for empty share
                                    int itemCount = 0;
                                    for(ItemStack item : Main.shareInvHM.get(pInfo.getShareCreator())){
                                        try{
                                            if(!item.equals(null)){
                                                itemCount++;
                                            }
                                        }catch(NullPointerException npe){
                                            continue;
                                        }
                                    }
                                    if(itemCount > 0){
                                        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + ChatColor.GOLD + "Your shared chest must be empty for you can disband.");
                                        sharePartner.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + player.getPlayerListName() + ChatColor.GOLD + " wants to disband your Ender Share, but your shared chest must be empty before you can disband.");
                                        return true;
                                    }else{

                                        //Check if their share partner has requested to disband.
                                        if(disbandCDList.containsKey(sharePartner)){ //Parter requested share
                                            //Disband the share
                                            //Remove from Main hashmap
                                            Main.shareInvHM.remove(pInfo.getShareCreator());

                                            //Update player file
                                            pInfo.setHasShare(false);
                                            pInfo.setShareCreator(null);
                                            pInfo.setSharePartner(null);
                                            //Update sharePartner file
                                            pInfo = new PlayerInfo(sharePartner, plugin);
                                            pInfo.setHasShare(false);
                                            pInfo.setShareCreator(null);
                                            pInfo.setSharePartner(null);
                                            //Alert players
                                            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + ChatColor.GOLD + "Your Ender Share with " + sharePartner.getPlayerListName() + ChatColor.GOLD + " has been disbanded!");
                                            sharePartner.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + ChatColor.GOLD + "Your Ender Share with " + player.getPlayerListName() + ChatColor.GOLD + " has been disbanded!");
                                            Bukkit.getScheduler().cancelTask(disbandCDList.get(sharePartner));
                                            disbandCDList.remove(sharePartner);

                                        }else{ //Request a disband
                                            if(disbandCDList.containsKey(player)){ //Already requested
                                                player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + ChatColor.GOLD + "You have already requested to disband your share with " + sharePartner.getPlayerListName() + ChatColor.GOLD + ".");
                                                return true;
                                            }else{ //No request active
                                                //Start 30 second cooldown
                                                BukkitTask task = new DisbandTimer(plugin, player, sharePartner).runTaskTimer(plugin, 0L, 30 * 20L);
                                                disbandCDList.put(player, task.getTaskId());

                                                //Alert players
                                                sharePartner.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + player.getPlayerListName() + ChatColor.GOLD +
                                                        " has requested to disband your Ender Share. Use " + ChatColor.GREEN + "" + ChatColor.BOLD + "/eshare disband" + ChatColor.GOLD +
                                                        " to accept. This request will expire in 30 seconds.");
                                                player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + ChatColor.GOLD + "Disband request sent to " + sharePartner.getPlayerListName() + ChatColor.GOLD + ". They have 30 seconds to accept.");
                                            }


                                        }


                                    }

                                    //Else no share.
                                }else {
                                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + ChatColor.GOLD + "You do not have a share to disband!");
                                    return true;
                                }
                                //No perms
                            }else{
                                player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + ChatColor.RED + "You do not have permission to use this command.");
                                return true;
                            }

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
                            //Check perms
                            if(player.hasPermission("enderShare.eShare.create")){
                                //Make sure the player does not already have a share
                                PlayerInfo pInfo = new PlayerInfo(player, plugin);
                                if(!pInfo.getHasShare()){
                                    //Check if the player already has an invite out
                                    if(shareCDList.containsKey(player)){
                                        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + ChatColor.GOLD + "You already have a share request sent. Please wait till it expires before sending another one!");
                                        return true;
                                    }

                                    //Start 30 second cooldown
                                    BukkitTask task = new ShareTimer(plugin, player, sharePartner).runTaskTimer(plugin, 0L, 30 * 20L);
                                    shareCDList.remove(player);
                                    shareCDList.put(player, task.getTaskId());
                                    //Alert the other player
                                    sharePartner.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + player.getPlayerListName() + ChatColor.GOLD +
                                            " has asked to share Ender Chests with you! You have 30 seconds to accept by using " + ChatColor.RED + "" + ChatColor.BOLD +
                                            "/eshare accept " + player.getName() + ChatColor.GOLD + "!");
                                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] "  + ChatColor.GOLD + "Share request sent to " + sharePartner.getPlayerListName() + ChatColor.GOLD + "!");
                                    pInfo.setReqTo(sharePartner.getUniqueId().toString());
                                }else{
                                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] "  + ChatColor.GOLD + "You already have a share. You must disband it before creating a new one!");
                                    return true;
                                }
                                //No perms
                            }else{
                                player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + ChatColor.RED + "You do not have permission to use this command.");
                                return true;
                            }

                            //Accept
                        }else if(subCmd.equals("ACCEPT")){
                            //Check perms
                            if(player.hasPermission("enderShare.eShare.accept")){
                                //Make sure neither player has a share
                                PlayerInfo pInfo = new PlayerInfo(player, plugin);
                                PlayerInfo spInfo = new PlayerInfo(sharePartner, plugin);

                                //Make sure the requesting player is online
                                if(!pInfo.getHasShare() && !spInfo.getHasShare()){
                                    if(!spInfo.getHasShare()){
                                        //Make sure share partner has sent a request to this person
                                        if(player.getUniqueId().toString().equals(spInfo.getReqTo())) {
                                            spInfo.setReqTo(null);
                                            if (shareCDList.containsKey(sharePartner)) {
                                                //Cancel cooldown
                                                Bukkit.getScheduler().cancelTask(shareCDList.get(sharePartner));
                                                shareCDList.remove(sharePartner);
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

                                //No perms
                            }else{
                                player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + ChatColor.RED + "You do not have permission to use this command.");
                                return true;
                            }

                        }
                        //Invalid args. Display a custom help message to the user.
                    }else{
                        player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "===== EnderShare =====");
                        player.sendMessage(ChatColor.GOLD + "Use: " + ChatColor.GREEN + "" + ChatColor.BOLD + "/eshare create <player> " + ChatColor.GOLD + "to send someone a share request!");
                        player.sendMessage(ChatColor.GOLD + "Use: " + ChatColor.GREEN + "" + ChatColor.BOLD + "/eshare accept <player> " + ChatColor.GOLD + "to accept a share request!");
                        player.sendMessage(ChatColor.GOLD + "Use: " + ChatColor.GREEN + "" + ChatColor.BOLD + "/eshare disband " + ChatColor.GOLD + "to disband your share with someone!");
                    }
                //No perms
                }else{
                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + ChatColor.RED + "You do not have permission to use this command.");
                    return true;
                }
            }
        }else{
            sender.sendMessage("[EnderShare] This command is only usable by players.");
        }
        return true;
    }



}
