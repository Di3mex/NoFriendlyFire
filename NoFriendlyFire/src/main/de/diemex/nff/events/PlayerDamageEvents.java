package de.diemex.nff.events;

import de.diemex.nff.NoFriendlyFire;
import de.diemex.nff.TeamMethods;
import de.diemex.nff.config.NFFCfg;
import de.diemex.nff.config.NFFNode;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Contains all Listeners related to Players
 */
public class PlayerDamageEvents implements Listener
{
    private final NoFriendlyFire plugin;
    private final NFFCfg nffCfg;

    public PlayerDamageEvents(NoFriendlyFire plugin)
    {
        this.plugin = plugin;
        nffCfg = plugin.getModuleForClass(NFFCfg.class);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageByEntityEvent event)
    {
        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();

        final boolean showFriendlyFireMsg = nffCfg.getBoolean(NFFNode.TEAMDMG_MSG_SHOW);
        final boolean showDamagedByMsg = nffCfg.getBoolean(NFFNode.DAMAGED_BY_MSG_SHOW);
        final String friendlyFireMsg = nffCfg.getString(NFFNode.TEAMDMG_MSG);
        final String damagedByMsg = nffCfg.getString(NFFNode.DAMAGED_BY_MSG);

        if (!(damagee instanceof Player)) return;
        if (!(damager instanceof Player))
        {
            //Damage is caused by the arrow not the player shooting it
            if (damager instanceof Arrow)
            {
                Arrow arrow = (Arrow) damager;
                if (arrow.getShooter() != null && arrow.getShooter() instanceof Player)
                {
                    damager = arrow.getShooter();
                } else //Skeleton
                {
                    return;
                }
            }
            else //If not damaged by player or damaged by arrow this doesn't interest us
            {
                return;
            }
        }
        Player pDamager = (Player)damager;
        Player pDamagee = (Player)damagee;

        TeamMethods teamMethods = plugin.getModuleForClass(TeamMethods.class);
        if (teamMethods.areOnSameTeam(pDamager, pDamagee))
        {
            event.setCancelled(true);
            if (showFriendlyFireMsg) pDamager.sendMessage(ChatColor.RED + friendlyFireMsg);
            if (showDamagedByMsg) pDamagee.sendMessage(ChatColor.RED + pDamager.getName() + " " + damagedByMsg);
        }
    }
}
