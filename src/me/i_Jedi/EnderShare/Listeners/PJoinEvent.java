package me.i_Jedi.EnderShare.Listeners;

import me.i_Jedi.EnderShare.Info.PlayerInfo;
import me.i_Jedi.EnderShare.Info.ShareInfo;
import me.i_Jedi.EnderShare.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PJoinEvent implements Listener {
    //Variables
    private JavaPlugin plugin;

    //Constructor
    public PJoinEvent(JavaPlugin jp){
        plugin = jp;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    //Event
    @EventHandler
    public void joinEvent(PlayerJoinEvent event){
        //Get player
        Player player = event.getPlayer();

        //Check if player has a share
        PlayerInfo pInfo = new PlayerInfo(player, plugin);
        if(pInfo.getHasShare()){
            //Put the share in the hashmap in main
            String cUUID = pInfo.getShareCreator();
            ShareInfo sInfo = new ShareInfo(plugin, player);
            Main.shareInvHM.put(cUUID, sInfo.getSharedInv());
        }
    }

}
