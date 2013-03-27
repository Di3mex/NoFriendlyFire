/*
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package de.diemex.nff.config;


import de.diemex.nff.NoFriendlyFire;
import de.diemex.nff.Team;
import de.diemex.nff.service.ConfigNode;
import de.diemex.nff.service.ModularConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;

import java.util.ArrayList;

/**
 * Configuration handler for the config.yml file.
 */
public class NFFCfg extends ModularConfig
{
    private Team[] teams;
    /**
     * @param plugin - plugin instance.
     */
    public NFFCfg(NoFriendlyFire plugin)
    {
        super(plugin);
    }

    @Override
    public void starting()
    {
        loadDefaults(plugin.getConfig());
        plugin.saveConfig();
        reload();
    }

    @Override
    public void closing()
    {
        plugin.reloadConfig();
        plugin.saveConfig();
    }

    @Override
    public void save()
    {
        plugin.saveConfig();
    }

    @Override
    public void set(String path, Object value)
    {
        final ConfigurationSection config = plugin.getConfig();
        config.set(path, value);
        plugin.saveConfig();
    }

    @Override
    public void reload()
    {
        plugin.reloadConfig();
        loadSettings(plugin.getConfig());
        boundsCheck();
        cleanRewrite();
        plugin.reloadConfig();
    }

    @Override
    public void loadSettings(ConfigurationSection config)
    {
        for (final NFFNode node : NFFNode.values())
        {
            updateOption(node);
        }
    }

    @Override
    public void loadDefaults(ConfigurationSection config)
    {
        for (NFFNode node : NFFNode.values())
        {
            //Team specific
            Object obj = config.get(node.getPath());
            MemorySection section = null;
            if (obj instanceof MemorySection)
                section = (MemorySection) obj;

            //General
            if (!config.contains(node.getPath()) && node.getVarType() != ConfigNode.VarType.TEAM_LIST)
            {
                config.set(node.getPath(), node.getDefaultValue());
            }

            //Team specific, empty list
            else if ((obj == null || section != null && section.getValues(true).size() == 0) && node.getVarType() == ConfigNode.VarType.TEAM_LIST)
            {
                //Write the name & color of all teams to the cfg
                ArrayList<Team> teams = (ArrayList) node.getDefaultValue();
                for (Team team : teams)
                {
                    config.set(NFFNode.TEAMS.getPath() + "." + team.getName() + ".Color", team.getColor().name());
                }
            }
        }
    }

    @Override
    public void boundsCheck(){}
}