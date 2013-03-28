/*
* This file is part of NoFriendlyFire
*
* Copyright (C) 2013 Di3mex
* NoFriendlyFire is licensed under the GNU General Public License.
*
* MyPet is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* NoFriendlyFire is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package de.diemex.nff.events;

import de.diemex.nff.NoFriendlyFire;
import de.diemex.nff.Team;
import de.diemex.nff.TeamMethods;
import de.diemex.nff.config.NFFCfg;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

import java.util.ArrayList;

/**
 * Color the name above a players head
 */
public class ColorMe implements Listener
{
    /**
     * Plugin Reference
     */
    NoFriendlyFire plugin;
    /**
     * Reference to the Config
     */
    NFFCfg cfg;

    /**
     * Standard Constructor
     * @param plugin
     */
    public ColorMe (NoFriendlyFire plugin)
    {
        this.plugin = plugin;
    }

    /**
     * When a player logs in give him the color of his team
     * @param event
     */
    @EventHandler
    public void onNameTag(PlayerReceiveNameTagEvent event) {
        TeamMethods teams = plugin.getModuleForClass(TeamMethods.class);
        Player player = event.getNamedPlayer();
        ArrayList<Team> team = teams.getTeams(player);
        if (team != null &&! team.isEmpty() &&! team.get(0).getColor().name().equals(ChatColor.WHITE.name()))
        {
            event.setTag(team.get(0).getColor() + player.getName());
        }
    }
}