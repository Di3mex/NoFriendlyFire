package de.diemex.nff.command;

import de.diemex.nff.NoFriendlyFire;
import de.diemex.nff.Team;
import de.diemex.nff.config.NFFCfg;
import de.diemex.nff.config.NFFNode;
import de.diemex.nff.service.ICommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;


/**
 * Teams command.
 */
public class TeamsCommand implements ICommand
{



   @Override
	    public boolean execute(NoFriendlyFire plugin, CommandSender sender, Command command, String label, String[] args)
	    {
		 	if (sender.hasPermission("nofriendlyfire." + "teams"))
		 	{
		 
		 		NFFCfg cfg = plugin.getModuleForClass(NFFCfg.class);
		 		cfg.closing();
		 		cfg.starting();
	        	
		 		final ArrayList<Team> teams = cfg.getTeams(NFFNode.TEAMS);
		 		
		 		for (Team team : teams)
		 		{  	
		 			
		 			ChatColor ccolor = team.getColor();
		 			String scolor = ccolor.name();
		 			String teamname = team.getName(); 
		 			sender.sendMessage(plugin.getTag() + teamname + " color is " + ccolor + scolor);
        	
		 		}

		 		return true;
		 	}else
		 	{
		 		
		 		sender.sendMessage(ChatColor.RED + plugin.getTag() + " You do not have acces to that command!");
		 		return true;
		 	}
	    }	
	
}
