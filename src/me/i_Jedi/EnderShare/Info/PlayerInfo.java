package me.i_Jedi.EnderShare.Info;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class PlayerInfo {
    //Variables
    private Player player;
    private File file;
    private FileConfiguration config;
    private JavaPlugin plugin;
    //Constructor
    public PlayerInfo(Player p, JavaPlugin jp){
        player = p;
        plugin = jp;
        file = new File(plugin.getDataFolder() + "/playerData/" + player.getUniqueId() + ".yml");
        config = YamlConfiguration.loadConfiguration(file);

        //If this file does not exist set the defaults for it
        if(!doesExist()){
            setDefaults();
        }
    }

    //Check if file exists
    public boolean doesExist(){
        if(file.exists()){
            return true;
        }
        return false;
    }

    //Save file
    public void saveFile(){
        try{
            config.save(file);
        }catch(IOException ioe){
            plugin.getLogger().info("PlayerInfo - Error saving file.");
        }
    }

    //Set defaults
    public void setDefaults(){
        config.set("hasShare", false);
        config.set("requestTo", null);
        config.set("sharePartner", null);
        config.set("shareCreator", null);
        saveFile();
    }

    //********** HASSHARE **********
    //Set has share
    public void setHasShare(boolean bool){
        config.set("hasShare", bool);
        saveFile();
    }

    //Get has share
    public boolean getHasShare(){
        try{
            return config.getBoolean("hasShare");
        }catch(NullPointerException npe){
            return false;
        }
    }

    //********* REQUEST TO **********
    //Set request to
    public void setReqTo(String UUID){
        config.set("requestTo", UUID);
        saveFile();
    }

    //Get request to
    public String getReqTo(){
        try{
            return config.getString("requestTo");
        }catch (NullPointerException npe){
            return "";
        }
    }

    //********** SHAREPARTNER **********
    //Set share partner
    public void setSharePartner(String UUID){
        config.set("sharePartner", UUID);
        saveFile();
    }

    //Get share partner
    public String getSharePartner(){
        try{
            return config.getString("sharePartner");
        }catch(NullPointerException npe){
            return "";
        }
    }

    //********** SHARECREATOR **********
    //Set share creator
    public void setShareCreator(String UUID){
        config.set("shareCreator", UUID);
    }

    //Get share creator
    public String getShareCreator(){
        try{
            return config.getString("shareCreator");
        }catch(NullPointerException npe){
            return "";
        }
    }
}
