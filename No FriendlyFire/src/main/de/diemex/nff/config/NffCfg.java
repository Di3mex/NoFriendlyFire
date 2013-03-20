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
import de.diemex.nff.service.ModularConfig;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Configuration handler for the root config.yml file.
 */
public class NffCfg extends ModularConfig
{

    /**
     * @param plugin - plugin instance.
     */
    public NffCfg(NoFriendlyFire plugin)
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
    }

    @Override
    public void loadSettings(ConfigurationSection config)
    {
        for (final NffNodes node : NffNodes.values())
        {
            updateOption(node);
        }
    }

    @Override
    public void loadDefaults(ConfigurationSection config)
    {
        for (NffNodes node : NffNodes.values())
        {
            if (!config.contains(node.getPath()))
            {
                config.set(node.getPath(), node.getDefaultValue());
            }
        }
    }

    @Override
    public void boundsCheck(){}
}