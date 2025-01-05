package com.xskyblock.models;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class IslandMember {
    private final Map<Permission, Boolean> permissions;

    public IslandMember() {
        this.permissions = new EnumMap<>(Permission.class);
        initializeDefaultPermissions();
    }

    private void initializeDefaultPermissions() {
        for (Permission permission : Permission.values()) {
            permissions.put(permission, false);
        }
    }

    public boolean hasPermission(Permission permission) {
        return permissions.getOrDefault(permission, false);
    }

    public void setPermission(Permission permission, boolean value) {
        permissions.put(permission, value);
    }

    public Map<Permission, Boolean> getAllPermissions() {
        return new EnumMap<>(permissions);
    }

    public List<String> getActivePermissions() {
        List<String> active = new ArrayList<>();
        for (Map.Entry<Permission, Boolean> entry : permissions.entrySet()) {
            if (entry.getValue()) {
                active.add(entry.getKey().name());
            }
        }
        return active;
    }
}