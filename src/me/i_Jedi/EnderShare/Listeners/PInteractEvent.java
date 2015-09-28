package me.i_Jedi.EnderShare.Listeners;

import me.i_Jedi.EnderShare.Info.PlayerInfo;
import me.i_Jedi.EnderShare.Info.ShareInfo;
import me.i_Jedi.EnderShare.Main;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PInteractEvent implements Listener {

    //Variables
    JavaPlugin plugin;

    //Constructor
    public PInteractEvent(JavaPlugin jp){
        plugin = jp;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    //Event
    @EventHandler
    public void pInteract(PlayerInteractEvent event){
        //Check for enderchest
        if(event.getClickedBlock().getType().equals(Material.ENDER_CHEST)){
            //Get player and check if they have a share
            Player player = event.getPlayer();
            PlayerInfo pInfo = new PlayerInfo(player, plugin);
            if(pInfo.getHasShare()){
                //Open the shared inventory for this player
                String cUUID = pInfo.getShareCreator();
                if(Main.shareInvHM.containsKey(cUUID)){
                    player.openInventory(Main.shareInvHM.get(cUUID));
                }else{
                    ShareInfo sInfo = new ShareInfo(plugin, player);
                    Main.shareInvHM.put(cUUID, sInfo.getSharedInv());
                    player.openInventory(Main.shareInvHM.get(cUUID));
                }
                player.playSound(player.getLocation(), Sound.CHEST_OPEN, 7, 1);
                event.setCancelled(true);
            }//Else do nothing special
        }

    }
}
