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

/**
 * Represents a configuration node.
 */
public interface ConfigNode
{

    /**
     * Get the config path.
     *
     * @return Config path.
     */
    public String getPath();

    /**
     * Get the variable type.
     *
     * @return Variable type.
     */
    public VarType getVarType();

    /**
     * Get the default value.
     *
     * @return Default value.
     */
    public Object getDefaultValue();

    /**
     * Variable Types.
     */
    public enum VarType
    {
        STRING,
        INTEGER,
        DOUBLE,
        BOOLEAN,
        LIST,
        DYNAMIC
    }

    /**
     * SubTypes, like percentage, y-value, custom etc
     */
    public enum SubType
    {
        PERCENTAGE,
        Y_VALUE,
        HEALTH
    }
}
