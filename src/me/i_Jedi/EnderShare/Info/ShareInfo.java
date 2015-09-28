package me.i_Jedi.EnderShare.Info;

import me.i_Jedi.EnderShare.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ShareInfo {

    //Variables
    private JavaPlugin plugin;
    private Player player;
    private File file;
    private FileConfiguration config;

    //Constructor
    public ShareInfo(JavaPlugin jp, Player p){
        plugin = jp;
        player = p;
        //Get share creator UUID
        PlayerInfo pInfo = new PlayerInfo(player, plugin);
        String creatorUUID = pInfo.getShareCreator();
        file = new File(plugin.getDataFolder() + "/shareData/" + creatorUUID + ".yml");
        config = YamlConfiguration.loadConfiguration(file);
    }

    //Save contents
    public void saveFile(){
        try{
            config.save(file);
        }catch (IOException ioe){
            plugin.getLogger().info("ShareInfo - Error saving share file.");
        }
    }

    //Create share file
    public void createShareFile(){
        //Store creator items first
        PlayerInfo pInfo = new PlayerInfo(player, plugin);
        String cUUID = pInfo.getShareCreator();
        Player cPlayer = Bukkit.getPlayer(UUID.fromString(cUUID));
        int slot = 0;
        for(ItemStack item : cPlayer.getEnderChest().getContents()){
            config.set("items."+ slot, item);
            slot++;
        }

        //Store player items
        for(ItemStack item: player.getEnderChest().getContents()){
            config.set("items." + slot, item);
            slot++;
        }

        //Clear both player's enderchest
        player.getEnderChest().clear();
        cPlayer.getEnderChest().clear();

        //Save
        saveFile();
        Main.shareInvHM.put(cUUID, getSharedInv());
    }

    //Get shared inventory
    public Inventory getSharedInv(){
        Inventory sInv = Bukkit.createInventory(null, 54, "Ender Share");

        for(String key : config.getConfigurationSection("items").getKeys(false)){
            int slot = Integer.parseInt(key);
            sInv.setItem(slot, config.getItemStack("items." + key));
        }
        return sInv;
    }

    //Save shared inventory
    public void saveInventory(){
        //Get creator UUID
        PlayerInfo pInfo = new PlayerInfo(player, plugin);
        String cUUID = pInfo.getShareCreator();
        config.set("items", null);
        int slot = 0;
        for(ItemStack item : Main.shareInvHM.get(cUUID).getContents()){
            config.set("items." + slot, item);
            slot++;
        }
        saveFile();
    }
}
