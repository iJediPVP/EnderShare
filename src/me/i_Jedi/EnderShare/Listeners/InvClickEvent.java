package me.i_Jedi.EnderShare.Listeners;

import me.i_Jedi.EnderShare.Info.ShareInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class InvClickEvent implements Listener {

    //Variables
    private JavaPlugin plugin;

    //Constructor
    public InvClickEvent(JavaPlugin jp){
        plugin = jp;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    //Event
    @EventHandler
    public void invClick(InventoryClickEvent event){
        if(event.getWhoClicked() instanceof Player){
            Player player = (Player) event.getWhoClicked();
            //Check name
            if(event.getInventory().getName().equals("Ender Share")){
                ShareInfo sInfo = new ShareInfo(plugin, player);
                sInfo.saveInventory();
            }
        }

    }
}
