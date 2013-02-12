/*
    NoFriendlyFire Bukkit Plugin By Diemex
    Copyright (C) 2013 Diemex

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.diemex.no.friendlyfire;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NoFriendlyFire extends JavaPlugin implements Listener{

	String msg_noFriendlyFire;
	String msg_DamagedBy;
	
	boolean config_showNoFriendlyFire;
	boolean config_showDamagedBy;
	
	FileConfiguration config;
	String configPath = "plugins" + File.separator + "NoFriendlyFire" + File.separator + "config.yml";
	
	@Override
	public void onEnable()
	{
		//Register the Event so we get notified when it's called
		getServer().getPluginManager().registerEvents(this, this);
		//Config to customize the messages
		config = YamlConfiguration.loadConfiguration(new File(configPath));
		FileConfiguration saveConfig = new YamlConfiguration();
		
		config_showNoFriendlyFire = config.getBoolean("NoFriendlyFire.FriendlyFireMsg.Show", true);
		msg_noFriendlyFire = 		config.getString("NoFriendlyFire.FriendlyFireMsg.MsgFriendlyFire", "Friendly Fire won't be tolerated!");
		config_showDamagedBy = 		config.getBoolean("NoFriendlyFire.DamagedByMsg.Show", false);
		msg_DamagedBy = 			config.getString("NoFriendlyFire.DamagedByMsg.MsgDamagedBy", "is a bad teammate, watch your back!");
		
		saveConfig.set("NoFriendlyFire.FriendlyFireMsg.Show", config_showNoFriendlyFire);
		saveConfig.set("NoFriendlyFire.FriendlyFireMsg.MsgFriendlyFire", msg_noFriendlyFire);
		saveConfig.set("NoFriendlyFire.DamagedByMsg.Show", config_showDamagedBy);
		saveConfig.set("NoFriendlyFire.DamagedByMsg.MsgDamagedBy", msg_DamagedBy);
		
		try
		{
			saveConfig.save(new File(configPath));
		}
		catch (IOException e)
		{
			getLogger().warning("Unable to write config to " + configPath);
		}
		
		super.onEnable();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	void onEntityDamage(EntityDamageByEntityEvent event){
		Entity damager = event.getDamager();
		Entity damagee = event.getEntity();
		//
		if (!(damagee instanceof Player)) return;
		if (!(damager instanceof Player)) 
			{
				//Damage is caused by the arrow not the player shooting it
				if (damager instanceof Arrow)
				{
					Arrow arrow = (Arrow) damager;
					if (arrow.getShooter() != null && arrow.getShooter() instanceof Player)
					{
						damager = arrow.getShooter();
					} else //Skeleton
					{
						return;
					}
				}
				else //If not damaged by player or damaged by arrow this doesn't interest us
				{
					return;
				}
			}
		Player pDamager = (Player)damager;
		Player pDamagee = (Player)damagee;
		if (areOnSameTeam(pDamager, pDamagee))
		{
			event.setCancelled(true);
			if (config_showNoFriendlyFire) pDamager.sendMessage(ChatColor.RED + msg_noFriendlyFire);
			if (config_showDamagedBy) pDamagee.sendMessage(ChatColor.RED + pDamager.getName() + " " + msg_DamagedBy);
		}
	}
	
	boolean areOnSameTeam (Player damager, Player damagee)
	{
		String teamDamager = null;
		String teamDamagee = null;
		//Check for up to 5 teams
		//Check Damager for permissions
		for (int i = 1; i <= 5; i++)
		{
			if (damager.hasPermission("nofriendlyfire.team"+i))
			{
				teamDamager = "nofriendlyfire.team"+i;
			}
			if (teamDamager != null) break; //out of the loop
		}
		//Check Damagee for permissions
		for (int i = 1; i <= 5; i++)
		{
			if (damagee.hasPermission("nofriendlyfire.team"+i))
			{
				teamDamagee = "nofriendlyfire.team"+i;
			}
			if (teamDamagee != null) break; //out of the loop
		}
		
		if (teamDamager != null && teamDamagee != null)
		{
			if (teamDamager.equalsIgnoreCase(teamDamagee))
			{
				return true; //Same team
			}
		}
		return false;
	}
}