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
        ArrayList<Team> teamDamager = getTeams(damager);
        ArrayList<Team> teamDamagee = getTeams(damagee);

        boolean sameTeam = false;

        if (teamDamager != null && teamDamagee != null)
        {
            for (Team team : teamDamager)
            {
                sameTeam = teamDamagee.contains(team);
                if (sameTeam) break;
            }
        }

        return sameTeam;
    }

    /**
     * Gets the team a player is on
     */
    public ArrayList<Team> getTeams (Player player)
    {
        final ArrayList<Team> teams = cfg.getTeams(NFFNode.TEAMS);
        ArrayList<Team> playerTeams = new ArrayList<Team>();

        for (Team team : teams)
        {
            if (player.hasPermission(team.getPermission()))
            {
                playerTeams.add(team);
            }
        }
        return playerTeams;
    }

    @Override
    public void starting(){/*ignored*/}

    @Override
    public void closing(){/*ignored*/}
}