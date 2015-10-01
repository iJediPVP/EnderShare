package me.i_Jedi.EnderShare.Timers;

import me.i_Jedi.EnderShare.Commands.EShareCom;
import me.i_Jedi.EnderShare.Info.PlayerInfo;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ShareTimer extends BukkitRunnable {

    //Variables
    private JavaPlugin plugin;
    private Player player, sharePlayer;

    //Constructor
    public ShareTimer(JavaPlugin jp, Player p, Player sp){
        plugin = jp;
        player = p;
        sharePlayer = sp;
    }

    private boolean bool = false;
    @Override
    public void run(){
        if(bool){
            sharePlayer.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + player.getPlayerListName() + ChatColor.GOLD + "'s share invite has expired!");
            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[EnderShare] " + ChatColor.GOLD + "Your share invite to " + sharePlayer.getPlayerListName() + ChatColor.GOLD + " has expired!");
            PlayerInfo pInfo = new PlayerInfo(player, plugin);
            pInfo.setReqTo("");
            EShareCom.shareCDList.remove(player);
            this.cancel();
        }else{
            bool = true;
        }

    }
}
