/*
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
package de.diemex.nff.service;

import de.diemex.nff.NoFriendlyFire;
import de.diemex.nff.Team;
import de.diemex.nff.config.NFFNode;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.print.DocFlavor;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Modular configuration class that utilizes a ConfigNode enumeration as easy
 * access and storage of configuration option values.
 *
 * @author Mitsugaru
 */
@SuppressWarnings("SameParameterValue")
public abstract class ModularConfig extends NFFModule
{
    /**
     * Cache of options for the config.
     */
    protected final Map<ConfigNode, Object> OPTIONS = new ConcurrentHashMap<ConfigNode, Object>();

    /**
     * Constructor.
     *
     * @param plugin - plugin instance.
     */
    public ModularConfig(NoFriendlyFire plugin)
    {
        super(plugin);
    }

    /**
     * This updates a configuration option from the file.
     *
     * @param node - ConfigNode to update.
     */
    @SuppressWarnings("unchecked")
    public void updateOption(final ConfigNode node)
    {
        final ConfigurationSection config = plugin.getConfig();
        switch (node.getVarType())
        {
            case LIST:
            {
                List<String> list = config.getStringList(node.getPath());
                if (list == null)
                {
                    list = (List<String>) node.getDefaultValue();
                }
                OPTIONS.put(node, list);
                break;
            }
            case DOUBLE:
            {
                OPTIONS.put(node, config.getDouble(node.getPath(), (Double) node.getDefaultValue()));
                break;
            }
            case STRING:
            {
                OPTIONS.put(node, config.getString(node.getPath(), (String) node.getDefaultValue()));
                break;
            }
            case INTEGER:
            {
                OPTIONS.put(node, config.getInt(node.getPath(), (Integer) node.getDefaultValue()));
                break;
            }
            case BOOLEAN:
            {
                OPTIONS.put(node, config.getBoolean(node.getPath(), (Boolean) node.getDefaultValue()));
                break;
            }
            case TEAM_LIST:
            {
                ArrayList<Team> teams = new ArrayList<Team>();
                ConfigurationSection section = config.getConfigurationSection(node.getPath());
                Map mapo = section.getValues(true);

                Iterator iter = mapo.entrySet().iterator();
                while (iter.hasNext())
                {
                    Team myTeam = new Team();
                    Map.Entry <String, Object> pair = (Map.Entry<String, Object>) iter.next();
                    String path = pair.getKey();
                    Object value = pair.getValue();
                    if (value instanceof ConfigurationSection)
                    {
                        ConfigurationSection colorSection = (ConfigurationSection) value;
                        //Parse the color
                        {
                            String colorStr = colorSection.getString("Color");
                            colorStr = colorStr.replace("#", "");
                            colorStr = colorStr.replaceAll("[^0-9A-Fa-f]", "0");//replace everything which isn't a valid hex symbol
                            //we need a length of 6
                            for (int i = colorStr.length(); i < 6; i++)
                                colorStr+="0"; //not efficient, you got a better solution? go ahead...
                            int red = Integer.parseInt(colorStr.substring(0, 2), 16);
                            int green = Integer.parseInt(colorStr.substring(2, 4), 16);
                            int blue = Integer.parseInt(colorStr.substring(4, 6), 16);
                            Color color = Color.fromRGB(red, green, blue);
                            myTeam.setColor(color);
                        }
                        myTeam.setName(path);
                        teams.add(myTeam);
                    }
                }
                //Teams empty? load defaults...
                if (teams.size() < 1)
                    teams = (ArrayList) node.getDefaultValue();

                OPTIONS.put(node, teams);
                break;
            }
            default:
            {
                OPTIONS.put(node, config.get(node.getPath(), node.getDefaultValue()));
            }
        }
    }

    /**
     * Rewrites the config based on the validated values which are in memory
     */
    public void cleanRewrite()
    {
        FileConfiguration config = new YamlConfiguration();
        Set <Map.Entry<ConfigNode, Object>> nodes = OPTIONS.entrySet();
        for (Map.Entry<ConfigNode, Object> entry : nodes)
        {
            ConfigNode node = entry.getKey();
            switch (node.getVarType())
            {
                case BOOLEAN:
                {
                    config.set(node.getPath(), getBoolean(node));
                    break;
                }
                case STRING:
                {
                    config.set(node.getPath(), getString(node));
                    break;
                }
                case INTEGER:
                {
                    config.set(node.getPath(), getInt(node));
                    break;
                }
                case DOUBLE:
                {
                    config.set(node.getPath(), getDouble(node));
                    break;
                }
                case LIST:
                {
                    config.set(node.getPath(), getStringList(node));
                    break;
                }
                case TEAM_LIST:
                {
                    ArrayList<Team> teams = getTeams(node);
                    for (Team team : teams)
                    {
                        String hexColor = String.format("#%06X", (0xFFFFFF & team.getColor().asRGB()));
                        config.set(NFFNode.TEAMS.getPath() + "." + team.getName() + ".Color", hexColor);
                    }
                }
                default:
                {
                    break;
                }
            }
        }
        try
        {
            config.save(new File(plugin.getDataFolder() + File.separator + "config.yml"));
        } catch (IOException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Saves the config.
     */
    public abstract void save();

    /**
     * Force set the value for the given configuration node.
     * <p/>
     * Note, there is no type checking with this method.
     *
     * @param node  - ConfigNode path to use.
     * @param value - Value to use.
     */
    public void set(final ConfigNode node, final Object value)
    {
        set(node.getPath(), value);
    }

    /**
     * Set the given path for the given value.
     *
     * @param path  - Path to use.
     * @param value - Value to use.
     */
    public abstract void set(final String path, final Object value);

    /**
     * Get the integer value of the node.
     *
     * @param node - Node to use.
     * @return Value of the node. Returns -1 if unknown.
     */
    public int getInt(final ConfigNode node)
    {
        int i = -1;
        switch (node.getVarType())
        {
            case INTEGER:
            {
                try
                {
                    i = ((Integer) OPTIONS.get(node)).intValue();
                } catch (NullPointerException npe)
                {
                    i = ((Integer) node.getDefaultValue()).intValue();
                }
                break;
            }
            default:
            {
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + node.getVarType() + " as an integer.");
            }
        }
        return i;
    }

    /**
     * Get the string value of the node.
     *
     * @param node - Node to use.
     * @return Value of the node. Returns and empty string if unknown.
     */
    public String getString(final ConfigNode node)
    {
        String out = "";
        switch (node.getVarType())
        {
            case STRING:
            {
                out = (String) OPTIONS.get(node);
                if (out == null)
                {
                    out = (String) node.getDefaultValue();
                }
                break;
            }
            default:
            {
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + node.getVarType() + " as a string.");
            }
        }
        return out;
    }

    /**
     * Get the list value of the node.
     *
     * @param node - Node to use.
     * @return Value of the node. Returns an empty list if unknown.
     */
    @SuppressWarnings("unchecked")
    public List<String> getStringList(final ConfigNode node)
    {
        List<String> list = new ArrayList<String>();
        switch (node.getVarType())
        {
            case LIST:
            {
                final ConfigurationSection config = plugin.getConfig();
                list = config.getStringList(node.getPath());
                if (list == null)
                {
                    list = (List<String>) node.getDefaultValue();
                }
                break;
            }
            default:
            {
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + node.getVarType() + " as a List<String>.");
            }
        }
        return list;
    }

    /**
     * Get the double value of the node.
     *
     * @param node - Node to use.
     * @return Value of the node. Returns 0 if unknown.
     */
    public double getDouble(final ConfigNode node)
    {
        double d = 0.0;
        switch (node.getVarType())
        {
            case DOUBLE:
            {
                try
                {
                    d = ((Double) OPTIONS.get(node)).doubleValue();
                } catch (NullPointerException npe)
                {
                    d = ((Double) node.getDefaultValue()).doubleValue();
                }
                break;
            }
            default:
            {
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + node.getVarType() + " as a double.");
            }
        }
        return d;
    }

    /**
     * Get the boolean value of the node.
     *
     * @param node - Node to use.
     * @return Value of the node. Returns false if unknown.
     */
    public boolean getBoolean(final ConfigNode node)
    {
        boolean b = false;
        switch (node.getVarType())
        {
            case BOOLEAN:
            {
                b = ((Boolean) OPTIONS.get(node)).booleanValue();
                break;
            }
            default:
            {
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + node.getVarType() + " as a boolean.");
            }
        }
        return b;
    }

    /**
     * Get the ArrayList of Teams
     * @param node
     * @return
     */
    public ArrayList<Team> getTeams(final ConfigNode node)
    {
        ArrayList<Team> teams = new ArrayList<Team>();
        switch (node.getVarType())
        {
            case TEAM_LIST:
            {
                teams = (ArrayList<Team>) OPTIONS.get(node);
                break;
            }
            default:
            {
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + node.getVarType() + " as a boolean.");
            }
        }
        return teams;
    }

    /**
     * Reloads info from yaml file(s).
     */
    public abstract void reload();

    /**
     * Update settings that can be changed on the fly.
     *
     * @param config - Main config to load from.
     */
    public abstract void loadSettings(final ConfigurationSection config);

    /**
     * Load defaults.
     *
     * @param config - Main config to load to.
     */
    public abstract void loadDefaults(final ConfigurationSection config);

    /**
     * Check the bounds on the parameters to make sure that all config variables
     * are legal and usable by the plugin.
     */
    public abstract void boundsCheck();
}
