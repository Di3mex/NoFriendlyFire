package de.diemex.nff;

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
    private Color color = Color.WHITE;

    private final String PREFIX = "nofriendlyfire.";

    public Team (/*Empty Constructor*/){};

    public Team( String name, Color color)
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

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
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
