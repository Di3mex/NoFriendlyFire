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
package de.diemex.nff;

import org.bukkit.ChatColor;
import org.bukkit.Color;

/**
 * This represents a Team.
 * <pre>
 * It can have a name which is used in the permission assigned to the team
 * and a color which gets shown in the name above the head of the player.
 * The name of the permission gets automatically based on the name of the team.
 * </pre>
 */
public class Team
{
    private String name = "";
    private String permission = ""; //Permission name based on the name of the Team
    private ChatColor color = ChatColor.WHITE;

    private final String PREFIX = "nofriendlyfire.";

    public Team (/*Empty Constructor*/){};

    public Team( String name, ChatColor color)
    {
        this.name = name;
        this.color = color;
        setPermission(PREFIX + name);
    }

    @Override
    public boolean equals (Object team)
    {
        if (team instanceof Team)
        {
            return ((Team)team).getName().equals(this.getName());
        }
        else
            return false;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ChatColor getColor()
    {
        return color;
    }

    public void setColor(ChatColor color)
    {
        this.color = color;
    }

    public String getPermission()
    {
        setPermission(PREFIX + name);
        return permission;
    }

    public void setPermission(String permission)
    {
        this.permission = permission;
    }
}
