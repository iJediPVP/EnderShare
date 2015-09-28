/*TODO LIST
Do disband functionality
Add custom help message to EShareCom
* */

package me.i_Jedi.EnderShare;

import me.i_Jedi.EnderShare.Commands.EShareCom;
import me.i_Jedi.EnderShare.Info.PlayerInfo;
import me.i_Jedi.EnderShare.Info.ShareInfo;
import me.i_Jedi.EnderShare.Listeners.InvClickEvent;
import me.i_Jedi.EnderShare.Listeners.InvCloseEvent;
import me.i_Jedi.EnderShare.Listeners.PInteractEvent;
import me.i_Jedi.EnderShare.Listeners.PJoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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

        //Register events
        new InvClickEvent(this);
        new InvCloseEvent(this);
        new PInteractEvent(this);
        new PJoinEvent(this);

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

    //Test commands
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        Player player = (Player) sender;
        String command = cmd.getName().toUpperCase();
        PlayerInfo pInfo = new PlayerInfo(player, this);
        if(command.equals("TEST")){
            String uuid = pInfo.getShareCreator();
            player.openInventory(shareInvHM.get(uuid));
        }else if(command.equals("TEST2")){

        }
        return true;
    }


}
