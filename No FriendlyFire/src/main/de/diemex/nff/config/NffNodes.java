package de.diemex.nff.config;

import de.diemex.nff.service.ConfigNode;

/**
 * All the
 */
public enum NffNodes implements ConfigNode
{
    TEAMDMG_MSG_SHOW
            ("NoFriendlyFire.FriendlyFireMsg.Show", VarType.BOOLEAN, "true"),
    TEAMDMG_MSG
            ("NoFriendlyFire.FriendlyFireMsg.MsgFriendlyFire", VarType.STRING, "Friendly Fire won't be tolerated!"),
    DAMAGED_BY_MSG_SHOW
            ("NoFriendlyFire.DamagedByMsg.Show", VarType.BOOLEAN, "false"),
    DAMAGED_BY_MSG
            ("NoFriendlyFire.DamagedByMsg.MsgDamagedBy", VarType.STRING, "is a bad teammate, watch your back!"),
    TEAMS
            ("NoFriendlyFire.Teams", VarType.DYNAMIC, null) //Just an accessor
    ;

    String path;
    VarType type;
    Object defaultValue;

    NffNodes (String path, VarType type, Object defaultValue)
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
}
