package de.diemex.nff.command;

import de.diemex.nff.NoFriendlyFire;
import de.diemex.nff.config.NFFCfg;
import de.diemex.nff.service.ICommand;
import de.diemex.nff.service.PermissionNode;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Reload command.
 */
public class ReloadCommand implements ICommand
{

    @Override
    public boolean execute(NoFriendlyFire plugin, CommandSender sender, Command command, String label, String[] args)
    {
        if (sender.hasPermission(PermissionNode.ADMIN.getNode()))
        {
            NFFCfg cfg = plugin.getModuleForClass(NFFCfg.class);
            cfg.closing();
            cfg.starting();
            sender.sendMessage(ChatColor.GREEN + plugin.getTag() + " Reloaded " + plugin.getName());
        }
        else
        {
            sender.sendMessage(ChatColor.RED + plugin.getTag() + " Lack permission: " + PermissionNode.ADMIN.getNode());
        }
        return true;
    }

}
