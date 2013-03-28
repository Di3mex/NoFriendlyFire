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
package de.diemex.nff.config;

import de.diemex.nff.Team;
import de.diemex.nff.service.ConfigNode;
import org.bukkit.ChatColor;

import java.util.ArrayList;

/**
 * All the
 */
public enum NFFNode implements ConfigNode
{
    BLOCK_SUICIDE
            ("NoFriendlyFire.Block.Suicide", VarType.BOOLEAN, false),
    BLOCK_MELEE
            ("NoFriendlyFire.Block.Melee", VarType.BOOLEAN, true),
    BLOCK_ARROWS
            ("NoFriendlyFire.Block.Arrows", VarType.BOOLEAN, true),
    BLOCK_SNOWBALLS
            ("NoFriendlyFire.Block.Snowballs", VarType.BOOLEAN, true),
    BLOCK_POTIONS
            ("NoFriendlyFire.Block.Potions", VarType.BOOLEAN, true),
    TEAMDMG_MSG_SHOW
            ("NoFriendlyFire.FriendlyFireMsg.Show", VarType.BOOLEAN, true),
    TEAMDMG_MSG
            ("NoFriendlyFire.FriendlyFireMsg.MsgFriendlyFire", VarType.STRING, "Friendly Fire won't be tolerated!"),
    DAMAGED_BY_MSG_SHOW
            ("NoFriendlyFire.DamagedByMsg.Show", VarType.BOOLEAN, false),
    DAMAGED_BY_MSG
            ("NoFriendlyFire.DamagedByMsg.MsgDamagedBy", VarType.STRING, "is a bad teammate, watch your back!"),
    TEAMS
            ("NoFriendlyFire.Teams", VarType.TEAM_LIST, new DefaultTeams())
    ;

    private String path;
    private VarType type;
    private Object defaultValue;

    NFFNode(String path, VarType type, Object defaultValue)
    {
        this.path = path;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getPath()
    {
        return path;
    }

    @Override
    public VarType getVarType()
    {
        return type;
    }

    @Override
    public Object getDefaultValue()
    {
        return defaultValue;
    }

    private static class DefaultTeams extends ArrayList<Team>
    {
        public DefaultTeams()
        {
            super();
            this.add(new Team("team1", ChatColor.WHITE));
            this.add(new Team("team2", ChatColor.WHITE));
            this.add(new Team("team3", ChatColor.WHITE));
            this.add(new Team("team4", ChatColor.WHITE));
            this.add(new Team("team5", ChatColor.WHITE));
        }
    }
}
