package com.xmyisland.models;

public enum Permission {
    BREAK("Allow break blocks"),
    PLACE("Allow place blocks"),
    USE("Allow use items"),
    ENTER("Allow enter island"),
    OPEN_CHESTS("Allow open chests"),
    ATTACK_MOBS("Allow attack mobs"),
    PVP("Allow PVP"),
    INTERACT_ENTITIES("Allow interact with entities"),
    MANAGE_PERMISSIONS("Allow manage permissions");

    private final String description;

    Permission(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}