package me.i_Jedi.EnderShare.Listeners;

import me.i_Jedi.EnderShare.Info.ShareInfo;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class InvCloseEvent implements Listener {
    //Variables
    private JavaPlugin plugin;

    //Constructor
    public InvCloseEvent(JavaPlugin jp){
        plugin = jp;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    //Event
    @EventHandler
    public void invClose(InventoryCloseEvent event){
        if(event.getInventory().getName().equals("Ender Share")){
            if(event.getPlayer() instanceof Player){
                Player player = (Player) event.getPlayer();
                ShareInfo sInfo = new ShareInfo(plugin, player);
                sInfo.saveInventory();
                player.playSound(player.getLocation(), Sound.CHEST_CLOSE, 7, 1);
            }

        }
    }
}
