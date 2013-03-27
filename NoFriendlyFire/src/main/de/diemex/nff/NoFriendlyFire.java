/*
    NoFriendlyFire Bukkit Plugin By Diemex
    Copyright (C) 2013 Diemex

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.diemex.nff;

import de.diemex.nff.config.NFFCfg;
import de.diemex.nff.config.NFFNode;
import de.diemex.nff.events.ColorMe;
import de.diemex.nff.events.PlayerDamageEvents;
import de.diemex.nff.service.IModule;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.tag.TagAPI;

import javax.swing.text.html.HTML;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NoFriendlyFire extends JavaPlugin implements Listener
{
    /**
     * Registered modules.
     */
    private final Map<Class<? extends IModule>, IModule> modules = new HashMap<Class<? extends IModule>, IModule>();
    /**
     * Config instance
     */
    private NFFCfg cfg;

	@Override
	public void onEnable()
	{
        super.onEnable();
        //Modules before EventListeners, because the listeners require some of the modules
        registerModule(NFFCfg.class, new NFFCfg(this));
        registerModule(TeamMethods.class, new TeamMethods(this));
        PluginManager pm = getServer().getPluginManager();
        //EventListeners
        pm.registerEvents(new PlayerDamageEvents(this), this);
        Plugin tagAPI = pm.getPlugin("TagAPI");
        if (tagAPI != null && tagAPI instanceof TagAPI)
            pm.registerEvents(new ColorMe(this), this);
        //Register all perms with default to none, so Ops don't have all Teams by default
        cfg = getModuleForClass(NFFCfg.class);
        ArrayList<Team> teams = cfg.getTeams(NFFNode.TEAMS);
        for (Team team : teams)
        {
            Permission perm = new Permission(team.getPermission(), PermissionDefault.FALSE);
            pm.addPermission(perm);
        }
	}


    /*
     * Modules
     */


    /**
     * Register a module.
     *
     * @param clazz  - Class of the instance.
     * @param module - Module instance.
     * @throws IllegalArgumentException - Thrown if an argument is null.
     */
    public <T extends IModule> void registerModule(Class<T> clazz, T module)
    {
        // Check arguments.
        if (clazz == null)
        {
            throw new IllegalArgumentException("Class cannot be null");
        }
        else if (module == null)
        {
            throw new IllegalArgumentException("Module cannot be null");
        }
        // Add module.
        modules.put(clazz, module);
        // Tell module to start.
        module.starting();
    }

    /**
     * Deregister a module.
     *
     * @param clazz - Class of the instance.
     * @return Module that was removed. Returns null if no instance of the module
     *         is registered.
     */
    public <T extends IModule> T deregisterModuleForClass(Class<T> clazz)
    {
        // Check arguments.
        if (clazz == null)
        {
            throw new IllegalArgumentException("Class cannot be null");
        }
        // Grab module and tell it its closing.
        T module = clazz.cast(modules.get(clazz));
        if (module != null)
        {
            module.closing();
        }
        return module;
    }

    /**
     * Retrieve a registered module.
     *
     * @param clazz - Class identifier.
     * @return Module instance. Returns null is an instance of the given class
     *         has not been registered with the API.
     */
    public <T extends IModule> T getModuleForClass(Class<T> clazz)
    {
        return clazz.cast(modules.get(clazz));
    }
}