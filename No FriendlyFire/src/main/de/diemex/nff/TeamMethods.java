package de.diemex.nff;

import de.diemex.nff.config.NFFCfg;
import de.diemex.nff.config.NFFNode;
import de.diemex.nff.service.NFFModule;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Logic for comparing teams and the sorts
 */
public class TeamMethods extends NFFModule
{
    NoFriendlyFire plugin;
    NFFCfg cfg;

    public TeamMethods (NoFriendlyFire plugin)
    {
        super(plugin);
        this.plugin = plugin;
        cfg = plugin.getModuleForClass(NFFCfg.class);
    }

    public boolean areOnSameTeam (Player damager, Player damagee)
    {
        Team teamDamager = getTeam(damager);
        Team teamDamagee = getTeam(damagee);

        if (teamDamager != null && teamDamagee != null)
        {
            return teamDamager.equals(teamDamagee);
        }

        return false;
    }

    /**
     * Gets the team a player is on
     */
    public Team getTeam (Player player)
    {
        final ArrayList<Team> teams = cfg.getTeams(NFFNode.TEAMS);
        Team playerTeam = null;

        for (Team team : teams)
        {
            if (player.hasPermission(team.getPermission()))
            {
                playerTeam = team;
                break;
            }
        }
        return playerTeam;
    }

    @Override
    public void starting(){/*ignored*/}

    @Override
    public void closing(){/*ignored*/}
}
