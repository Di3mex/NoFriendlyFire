package de.diemex.nff.command;

import java.util.ArrayList;

import de.diemex.nff.NoFriendlyFire;
import de.diemex.nff.Team;
import de.diemex.nff.config.NFFCfg;
import de.diemex.nff.config.NFFNode;
import de.diemex.nff.service.ICommand;
import de.diemex.nff.service.PermissionNode;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Color command.
 */
public class ColorCommand implements ICommand
{

  
	 @Override
	    public boolean execute(NoFriendlyFire plugin, CommandSender sender, Command command, String label, String[] args)
	    {
	        if (sender.hasPermission(PermissionNode.ADMIN.getNode()))
	        {
	        	
	        	NFFCfg cfg = plugin.getModuleForClass(NFFCfg.class);
	        
	        	final ArrayList<Team> teams = cfg.getTeams(NFFNode.TEAMS);
	        	String nteam = args[0];
	        	String ncolor = args[1];
	        	ncolor = ncolor.toUpperCase();
	        	
	        	for (Team team : teams)
	        	{
		 		
	        		String teamname = team.getName();
	        		
	        		if (nteam.equals(teamname))
	        		{
	        			cfg.set(NFFNode.TEAMS.getPath() + "." + nteam + ".Color", ncolor);
	    	        	cfg.closing();
	    	        	cfg.starting();
	        			sender.sendMessage(ChatColor.GREEN + plugin.getTag() + " " + nteam + " color is changed!");
	        			return true;
	        		}
	        		
	        	}
	        	sender.sendMessage(ChatColor.RED + plugin.getTag() + " This team is not created!");
	 			return true;
	        }else
	        {
	        	sender.sendMessage(ChatColor.RED + plugin.getTag() + " You are not admin!");
	        	return true;
	        }
	    }
	 
	
}
