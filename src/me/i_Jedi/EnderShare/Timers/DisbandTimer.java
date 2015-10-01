package me.i_Jedi.EnderShare.Timers;

import me.i_Jedi.EnderShare.Commands.EShareCom;
import me.i_Jedi.EnderShare.Info.PlayerInfo;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DisbandTimer extends BukkitRunnable{

    //Variables
    private JavaPlugin plugin;
    private Player player, sharePlayer;

    //Constructor
    public DisbandTimer(JavaPlugin jp, Player p, Player sp){
        plugin = jp;
        player = p;
        sharePlayer = sp;
    }

    private boolean bool = false;
    @Override
    public void run(){
        if(bool){
            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + ChatColor.GOLD + "Your disband request to " + sharePlayer.getPlayerListName() + ChatColor.GOLD + " has expired.");
            sharePlayer.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + ChatColor.GOLD + "The disband request from " + player.getPlayerListName() + " has expired.");
            PlayerInfo pInfo = new PlayerInfo(player, plugin);
            EShareCom.disbandCDList.remove(player);
            this.cancel();
        }else{
            bool = true;
        }

    }
}
