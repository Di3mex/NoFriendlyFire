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
