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