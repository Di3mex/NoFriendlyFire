package de.diemex.nff.command;

import de.diemex.nff.NoFriendlyFire;
import de.diemex.nff.Team;
import de.diemex.nff.config.NFFCfg;
import de.diemex.nff.config.NFFNode;
import de.diemex.nff.service.ICommand;
import de.diemex.nff.service.PermissionNode;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;


/**
 * List all Teams
 */
public class TeamsCommand implements ICommand
{
    @Override
    public boolean execute(NoFriendlyFire plugin, CommandSender sender, Command command, String label, String[] args)
    {
        if (sender.hasPermission(PermissionNode.ADMIN.getNode()))
        {

            NFFCfg cfg = plugin.getModuleForClass(NFFCfg.class);

            final ArrayList<Team> teams = cfg.getTeams(NFFNode.TEAMS);

            StringBuilder builder = new StringBuilder();

            for (Team team : teams)
            {
                if (builder.length() != 0) //not first iteration
                {
                    builder.append(", ");
                }
                builder.append(team.getColor());
                builder.append(team.getName());
            }

            sender.sendMessage(plugin.getTag() + builder.toString());

            return true;
        }else
        {

            sender.sendMessage(ChatColor.RED + plugin.getTag() + " You do not have acces to that command!");
            return true;
        }
    }
}