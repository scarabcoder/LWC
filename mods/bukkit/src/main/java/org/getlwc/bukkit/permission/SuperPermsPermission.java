package org.getlwc.bukkit.permission;

import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.getlwc.entity.Player;
import org.getlwc.permission.Permission;

import java.util.HashSet;
import java.util.Set;

public class SuperPermsPermission implements Permission {

    /**
     * The prefix for groups when using permissions
     */
    private static final String GROUP_PREFIX = "group.";

    public boolean isEnabled() {
        return true;
    }

    public boolean hasPermission(Player player, String permission) {
        org.bukkit.entity.Player handle = Bukkit.getPlayer(player.getName());
        return handle != null && handle.hasPermission(permission);
    }

    public Set<String> getGroups(Player player) {
        org.bukkit.entity.Player handle = Bukkit.getPlayer(player.getName());
        Set<String> groups = new HashSet<String>();

        for (PermissionAttachmentInfo pai : handle.getEffectivePermissions()) {
            if (pai.getPermission().startsWith(GROUP_PREFIX)) {
                groups.add(pai.getPermission().substring(GROUP_PREFIX.length()));
            }
        }

        return groups;
    }

}