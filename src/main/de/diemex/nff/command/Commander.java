package de.diemex.nff.command;


import de.diemex.nff.NoFriendlyFire;
import de.diemex.nff.service.CommandHandler;
import de.diemex.nff.service.ICommand;
import de.diemex.nff.service.PermissionNode;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Commander extends CommandHandler
{

    private final NoFriendlyFire plugin;

    public Commander(NoFriendlyFire plugin)
    {
        super(plugin, "nff");
        this.plugin = plugin;
        registerCommand("reload", new ReloadCommand());
        registerCommand("teams", new TeamsCommand());
        registerCommand("add", new AddCommand());
        registerCommand("remove", new RemoveCommand());
        registerCommand("color", new ColorCommand());
    }

    @Override
    public boolean noArgs(CommandSender sender, Command command, String label)
    {
        sender.sendMessage(ChatColor.GRAY + "========= " + ChatColor.RED + plugin.getName() + ChatColor.GRAY + " =========");
        sender.sendMessage(" /nff");
        if (sender.hasPermission(PermissionNode.ADMIN.getNode()))
        {//TODO why is it not in colums?! dafuq
            sender.sendMessage("    reload   " + ChatColor.YELLOW + "- Reload the plugin");
            sender.sendMessage("    teams    " + ChatColor.YELLOW + "- List all teams");
            sender.sendMessage("    add      " + ChatColor.YELLOW + "- [team] [color]");
            sender.sendMessage("    remove   " + ChatColor.YELLOW + "- [team]");
            sender.sendMessage("    color    " + ChatColor.YELLOW + "- [team] [color]");
        }
        return true;
    }

    @Override
    public boolean unknownCommand(CommandSender sender, Command command, String label, String[] args)
    {
        sender.sendMessage(ChatColor.YELLOW + plugin.getTag() + " Unknown command: " + ChatColor.WHITE + args[0]);
        return true;
    }

    private class HelpCommand implements ICommand
    {

        @Override
        public boolean execute(NoFriendlyFire plugin, CommandSender sender, Command command, String label, String[] args)
        {
            return noArgs(sender, command, label);
        }

    }
}
