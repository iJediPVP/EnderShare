package me.i_Jedi.EnderShare.Listeners;

import me.i_Jedi.EnderShare.Commands.EShareCom;
import me.i_Jedi.EnderShare.Info.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PQuitEvent implements Listener {

    //Variables
    private JavaPlugin plugin;

    //Constructor
    public PQuitEvent(JavaPlugin jp){
        plugin = jp;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    //Event
    @EventHandler
    public void pQuit(PlayerQuitEvent event){
        //Get player and check if they have a share request. If they do cancel it and notify the other player involved
        Player player = event.getPlayer();
        PlayerInfo pInfo = new PlayerInfo(player, plugin); //npe
        try{
            if(!pInfo.getReqTo().equals("")){
                if(EShareCom.cdList.containsKey(player)){
                    plugin.getServer().getScheduler().cancelTask(EShareCom.cdList.get(player));
                    EShareCom.cdList.remove(player);
                    pInfo.setReqTo(null);
                    //Check if the other player is online
                }
            }
        }catch(NullPointerException npe){
            return;
        } //Do nothing with it. The player did not have a share request sent


    }
}
