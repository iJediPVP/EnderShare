/*TODO LIST

* */

package me.i_Jedi.EnderShare;

import me.i_Jedi.EnderShare.Commands.EShareCom;
import me.i_Jedi.EnderShare.Commands.EShareTabComplete;
import me.i_Jedi.EnderShare.Info.PlayerInfo;
import me.i_Jedi.EnderShare.Info.ShareInfo;
import me.i_Jedi.EnderShare.Listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Main extends JavaPlugin{

    public static HashMap<String, Inventory> shareInvHM = new HashMap<>();

    //Enabled
    public void onEnable(){
        //Register commands
        this.getCommand("ESHARE").setExecutor(new EShareCom(this));
        this.getCommand("ESHARE").setTabCompleter(new EShareTabComplete());
        //Register events
        new InvClickEvent(this);
        new InvCloseEvent(this);
        new PInteractEvent(this);
        new PJoinEvent(this);
        new PQuitEvent(this);

        //Close all open ender share chests (prevent duplication)
        for(Player p : Bukkit.getOnlinePlayers()){
            PlayerInfo pInfo = new PlayerInfo(p, this);
            ShareInfo sInfo = new ShareInfo(this, p);
            if(pInfo.getHasShare()){
                shareInvHM.put(pInfo.getShareCreator(), sInfo.getSharedInv());
                if(p.getOpenInventory().getTitle().equals("Ender Share")) {
                    p.closeInventory();
                    sInfo.saveInventory();
                }

            }

        }

        //Log
        getLogger().info("EnderShare has been enabled!");
    }

    //Disabled
    public void onDisable(){
        //Log
        getLogger().info("EnderShare has been disabled!");
    }



}
