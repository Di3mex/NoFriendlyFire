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
import java.util.logging.Logger;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.entity.LivingEntity;


public class NoFriendlyFire extends JavaPlugin implements Listener{

Logger log = Logger.getLogger("Minecraft");	
String msg_noFriendlyFire;
String msg_DamagedBy;

boolean config_showNoFriendlyFire;
boolean config_showDamagedBy;
boolean config_commitsuicide;
boolean commitsuicide = false;
FileConfiguration config;
String configPath = "plugins" + File.separator + "NoFriendlyFire" + File.separator + "config.yml";

@Override
public void onEnable()
{
	log.info("Hy,NoFriendlyFire is working!!! :D");
	//Register the Event so we get notified when it's called
	getServer().getPluginManager().registerEvents(this, this);
	//Config to customize the messages
	config = YamlConfiguration.loadConfiguration(new File(configPath));
	FileConfiguration saveConfig = new YamlConfiguration();
	
	config_showNoFriendlyFire = config.getBoolean("NoFriendlyFire.FriendlyFireMsg.Show", true);
	msg_noFriendlyFire = 		config.getString("NoFriendlyFire.FriendlyFireMsg.MsgFriendlyFire", "Friendly Fire won't be tolerated!");
	config_showDamagedBy = 		config.getBoolean("NoFriendlyFire.DamagedByMsg.Show", false);
	msg_DamagedBy = 			config.getString("NoFriendlyFire.DamagedByMsg.MsgDamagedBy", "is a bad teammate, watch your back!");
	config_commitsuicide = 		config.getBoolean("NoFriendlyFire.Commitsuicide.Enabled", false);
	
	saveConfig.set("NoFriendlyFire.FriendlyFireMsg.Show", config_showNoFriendlyFire);
	saveConfig.set("NoFriendlyFire.FriendlyFireMsg.MsgFriendlyFire", msg_noFriendlyFire);
	saveConfig.set("NoFriendlyFire.DamagedByMsg.Show", config_showDamagedBy);
	saveConfig.set("NoFriendlyFire.DamagedByMsg.MsgDamagedBy", msg_DamagedBy);
	saveConfig.set("NoFriendlyFire.Commitsuicide.Enabled", config_commitsuicide);
	
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


private static final Set<PotionEffectType> badPotionEffects = new LinkedHashSet<PotionEffectType>(Arrays.asList(
PotionEffectType.BLINDNESS, PotionEffectType.CONFUSION, PotionEffectType.HARM, PotionEffectType.HUNGER,
PotionEffectType.POISON, PotionEffectType.SLOW, PotionEffectType.SLOW_DIGGING, PotionEffectType.WEAKNESS,
PotionEffectType.WITHER
));



@EventHandler(priority = EventPriority.NORMAL)
public void onPotionSplashEvent(PotionSplashEvent event)
{
	if (event.isCancelled()) return;

	//see if the potion has a harmful effect
	boolean badjuju = false;
	for (PotionEffect effect : event.getPotion().getEffects())
	{
		if (badPotionEffects.contains(effect.getType()))
		{
			badjuju = true;
			break;
		}
	}
	if ( ! badjuju) return;

	Entity damager = event.getPotion().getShooter();
	
	//scan through affected entities to make sure they're all valid targets
	Iterator<LivingEntity> iter = event.getAffectedEntities().iterator();
	while (iter.hasNext())
	{
		commitsuicide = false;
		LivingEntity damagee = iter.next();
		
		if (config_commitsuicide)
		{
			if (damager == damagee)
			{
				commitsuicide = true; 
				
			}
		}
		
		
		if (damager instanceof Player && !(commitsuicide))
		{
		
			Player pDamager = (Player)damager;
			Player pDamagee = (Player)damagee;
			
			if (areOnSameTeam(pDamager, pDamagee))
			{
				event.setIntensity(damagee, 0.0);
				if (!(pDamager == pDamagee))
				{
					if (config_showNoFriendlyFire) pDamager.sendMessage(ChatColor.RED + msg_noFriendlyFire);
					if (config_showDamagedBy) pDamagee.sendMessage(ChatColor.RED + pDamager.getName() + " " + msg_DamagedBy);
				}
			}
		}		
	}
}









@EventHandler(priority = EventPriority.LOWEST)
void onEntityDamage(EntityDamageByEntityEvent event){
	Entity damager = event.getDamager();
	Entity damagee = event.getEntity();	
	

	
	if (!(damagee instanceof Player)) return;
	
	if (damager instanceof Arrow)
	{
		Arrow arrow = (Arrow) damager;
	if (arrow.getShooter() != null && arrow.getShooter() instanceof Player)
	{
		damager = arrow.getShooter();
	}
	}
	
	if (damager instanceof Snowball)
	{
		Snowball snowball = (Snowball) damager;
	if (snowball.getShooter() != null && snowball.getShooter() instanceof Player)
	{
		damager = snowball.getShooter();
	}
	}
	
	
	if (config_commitsuicide)
	{
		if (damager == damagee)
		{
			return;
		}
		
		
	}else
	{
		if (damager == damagee)
		{
			event.setCancelled(true);
			return;
		}
	}
	
	
	
	
	
	if (damager instanceof Player)
	{
	
		Player pDamager = (Player)damager;
		Player pDamagee = (Player)damagee;
		
		if (areOnSameTeam(pDamager, pDamagee))
		{
			event.setCancelled(true);
			if (config_showNoFriendlyFire) pDamager.sendMessage(ChatColor.RED + msg_noFriendlyFire);
			if (config_showDamagedBy) pDamagee.sendMessage(ChatColor.RED + pDamager.getName() + " " + msg_DamagedBy);
		}
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
