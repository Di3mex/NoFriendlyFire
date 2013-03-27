package de.diemex.nff.service;

import java.util.ArrayList;

/**
 * Dynmaic PermissionsManager:
 *
 * Holds all the Permissions
 */
public class PermissionStorage
{
    private ArrayList <String> permissionNodes = new ArrayList<String>();
    private String PREFIX = "NoFriendlyFire";

    /**
     * Adds a permissions to the storage. Bare in mind that the prefix will be prepended
     * when you get the permission again.
     *
     * @param node
     */
    public void addNode (String node)
    {
        permissionNodes.add(PREFIX  + node);
    }

    public ArrayList<String> getPermissionNodes()
    {
        return permissionNodes;
    }
}
