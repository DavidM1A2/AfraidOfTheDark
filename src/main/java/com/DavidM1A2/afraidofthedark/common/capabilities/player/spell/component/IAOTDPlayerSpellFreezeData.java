package com.DavidM1A2.afraidofthedark.common.capabilities.player.spell.component;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

/**
 * An interface that stores data that lets us freeze the player
 */
public interface IAOTDPlayerSpellFreezeData
{
    /**
     * Sets the number of ticks the player should be frozen
     *
     * @param numberOfTicks The number of ticks
     */
    void setFreezeTicks(int numberOfTicks);

    /**
     * Gets the number of ticks the player should be frozen
     *
     * @return The number of ticks
     */
    int getFreezeTicks();

    /**
     * Sets the position to freeze the player at
     *
     * @param position The position to freeze the player at
     */
    void setFreezePosition(Vec3d position);

    /**
     * Gets the position the player is frozen at
     *
     * @return The position the player is frozen at
     */
    Vec3d getFreezePosition();

    /**
     * Sets the direction the player was looking when frozen
     *
     * @param yaw The yaw of the direction the player is looking
     * @param pitch The pitch of the direction the player is looking
     */
    void setFreezeDirection(float yaw, float pitch);

    /**
     * Gets the yaw of the direction the player is frozen towards
     *
     * @return The yaw that the player was looking when frozen
     */
    float getFreezeYaw();

    /**
     * Gets the pitch of the direction the player is frozen towards
     *
     * @return The pitch that the player was looking when frozen
     */
    float getFreezePitch();

    /**
     * Synchronizes freeze data between server and client
     *
     * @param entityPlayer The player to sync freeze data to
     */
    void sync(EntityPlayer entityPlayer);
}
