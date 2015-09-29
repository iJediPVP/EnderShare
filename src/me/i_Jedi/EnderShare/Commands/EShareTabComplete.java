package me.i_Jedi.EnderShare.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EShareTabComplete implements TabCompleter {

    //Completer
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args){
        //Check for player
        if(sender instanceof Player){
            List<String> tabList = new ArrayList<>();
            //Check command
            String command = cmd.getName().toUpperCase();
            if(command.equals("ESHARE")){
                //Check args
                if(args.length == 1){ //Return sub commands
                    tabList.add("create");
                    tabList.add("accept");
                    tabList.add("disband");
                    return tabList;
                }
            }
        }
        return null;
    }
}
