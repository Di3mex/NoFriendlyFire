package de.diemex.nff.events;

import de.diemex.nff.NoFriendlyFire;
import de.diemex.nff.TeamMethods;
import de.diemex.nff.config.NFFCfg;
import de.diemex.nff.config.NFFNode;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Contains all Listeners related to Players
 */
public class PlayerDamageEvents implements Listener
{
    private final NoFriendlyFire plugin;
    private final NFFCfg nffCfg;
    TeamMethods teamLogic;

    public PlayerDamageEvents(NoFriendlyFire plugin)
    {
        this.plugin = plugin;
        nffCfg = plugin.getModuleForClass(NFFCfg.class);
        teamLogic = plugin.getModuleForClass(TeamMethods.class);
    }

    /**
     * When an Entity get damaged by another Entity. Arrows and Snowballs are Entities too!
     * @param event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageByEntityEvent event)
    {
        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();

        final boolean showFriendlyFireMsg = nffCfg.getBoolean(NFFNode.TEAMDMG_MSG_SHOW);
        final boolean showDamagedByMsg = nffCfg.getBoolean(NFFNode.DAMAGED_BY_MSG_SHOW);
        final String friendlyFireMsg = nffCfg.getString(NFFNode.TEAMDMG_MSG);
        final String damagedByMsg = nffCfg.getString(NFFNode.DAMAGED_BY_MSG);
        final boolean allowSuicide = !nffCfg.getBoolean(NFFNode.BLOCK_SUICIDE);
        final boolean blockMelee = nffCfg.getBoolean(NFFNode.BLOCK_MELEE);
        final boolean blockArrows = nffCfg.getBoolean(NFFNode.BLOCK_ARROWS);
        final boolean blockSnowballs = nffCfg.getBoolean(NFFNode.BLOCK_SNOWBALLS);

        //Allow to only block Arrows for example
        boolean damagedByArrow = false, damagedBySnowball = false;

        if (damagee instanceof Player)
        {
            if (!(damager instanceof Player))
            {
                //Damage is caused by the arrow/snowball not the player shooting it
                if (damager instanceof Arrow && blockArrows)
                {
                    Arrow arrow = (Arrow) damager;
                    if (arrow.getShooter() != null && arrow.getShooter() instanceof Player)
                    {
                        damager = arrow.getShooter();
                        damagedByArrow = true;
                    }
                    else //Skeleton
                    {
                        return;
                    }
                }
                else if (damager instanceof Snowball && blockSnowballs)
                {
                    Snowball snowball = (Snowball) damager;
                    if (snowball.getShooter() != null && snowball.getShooter() instanceof Player)
                    {
                        damager = snowball.getShooter();
                        damagedBySnowball = true;
                    }
                    else //snowgolem, autoturret etc...
                    {
                        return;
                    }
                }
                else //If not damaged by player or damaged by arrow this doesn't interest us
                {
                    return;
                }
            }

            //For all the idiots that try to kill themselves with arrows :D
            if (damager == damagee)
            {
                if (!allowSuicide)
                    event.setCancelled(true);
                return;
            }

            if (damager instanceof Player)
            {
                if (damagedByArrow || damagedBySnowball || blockMelee) //Allow to selectively block things
                {
                    Player pDamager = (Player)damager;
                    Player pDamagee = (Player)damagee;

                    if (teamLogic.areOnSameTeam(pDamager, pDamagee))
                    {
                        event.setCancelled(true);
                        if (showFriendlyFireMsg) pDamager.sendMessage(ChatColor.RED + friendlyFireMsg);
                        if (showDamagedByMsg) pDamagee.sendMessage(ChatColor.RED + pDamager.getName() + " " + damagedByMsg);
                    }
                }
            }
        }
    }

    /**
     * When a Potion splashes, a Player/Witch/Dispenser could trigger the Event
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPotionSplashEvent(PotionSplashEvent event)
    {
        final Set<PotionEffectType> badPotionEffects = new LinkedHashSet<PotionEffectType>(Arrays.asList(
                PotionEffectType.BLINDNESS,
                PotionEffectType.CONFUSION,
                PotionEffectType.HARM,
                PotionEffectType.HUNGER,
                PotionEffectType.POISON,
                PotionEffectType.SLOW,
                PotionEffectType.SLOW_DIGGING,
                PotionEffectType.WEAKNESS,
                PotionEffectType.WITHER
        ));

        final boolean blockPotions = nffCfg.getBoolean(NFFNode.BLOCK_POTIONS);
        final boolean allowSuicide = !nffCfg.getBoolean(NFFNode.BLOCK_SUICIDE);
        final boolean showFriendlyFireMsg = nffCfg.getBoolean(NFFNode.TEAMDMG_MSG_SHOW);
        final boolean showDamagedByMsg = nffCfg.getBoolean(NFFNode.DAMAGED_BY_MSG_SHOW);
        final String friendlyFireMsg = nffCfg.getString(NFFNode.TEAMDMG_MSG);
        final String damagedByMsg = nffCfg.getString(NFFNode.DAMAGED_BY_MSG);

        if (blockPotions)
        {
            //see if the potion has a harmful effect
            boolean badPotionEffect = false;
            for (PotionEffect effect : event.getPotion().getEffects())
            {
                if (badPotionEffects.contains(effect.getType()))
                {
                    badPotionEffect = true;
                    break;
                }
            }

            if (badPotionEffect)
            {
                boolean commitSuicide = false;

                Entity damager = event.getPotion().getShooter();

                //scan through affected entities to make sure they're all valid targets
                Iterator<LivingEntity> iter = event.getAffectedEntities().iterator();
                while (iter.hasNext())
                {
                    commitSuicide = false;
                    LivingEntity damagee = iter.next();

                    if (allowSuicide)
                    {
                        if (damager == damagee)
                        {
                            commitSuicide = true;
                        }
                    }


                    if (damager instanceof Player &&! commitSuicide)
                    {
                        Player pDamager = (Player) damager;
                        Player pDamagee = (Player) damagee;

                        if (teamLogic.areOnSameTeam(pDamager, pDamagee))
                        {
                            event.setIntensity(damagee, 0.0);
                            if (!(pDamager == pDamagee))
                            {
                                if (showFriendlyFireMsg)
                                    pDamager.sendMessage(ChatColor.RED + friendlyFireMsg);
                                if (showDamagedByMsg)
                                    pDamagee.sendMessage(ChatColor.RED + pDamager.getName() + " " + damagedByMsg);
                            }
                        }
                    }
                }
            }
        }
    }
}