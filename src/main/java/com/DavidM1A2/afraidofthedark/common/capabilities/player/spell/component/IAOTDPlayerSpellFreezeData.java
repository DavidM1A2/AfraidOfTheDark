package com.DavidM1A2.afraidofthedark.common.capabilities.player.spell.component;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec2f;
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
     * @param direction The direction the player is looking, x is yaw and y is pitch
     */
    void setFreezeDirection(Vec2f direction);

    /**
     * Gets the direction the player is frozen towards
     *
     * @return The direction the player was looking when frozen, x is yaw and y is pitch
     */
    Vec2f getFreezeDirection();

    /**
     * Synchronizes freeze data between server and client
     *
     * @param entityPlayer The player to sync freeze data to
     */
    void sync(EntityPlayer entityPlayer);
}
