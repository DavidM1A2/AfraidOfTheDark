package com.DavidM1A2.afraidofthedark.common.capabilities.player.spell.component;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.packets.capabilityPackets.SyncFreezeData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;

/**
 * Default implementation of the AOTD spell freeze data class
 */
public class AOTDPlayerSpellFreezeDataImpl implements IAOTDPlayerSpellFreezeData
{
    // The number of freeze ticks remaining
    private int freezeTicksRemaining = 0;
    // The position the player is frozen at
    private Vec3d position;
    // The direction the player is facing when frozen
    private float yaw;
    private float pitch;

    /**
     * Returns true if the player is on server side or false if not
     *
     * @param entityPlayer The player to test
     * @return true if the player is on server side or false if not
     */
    private boolean isServerSide(EntityPlayer entityPlayer)
    {
        return !entityPlayer.world.isRemote;
    }

    /**
     * Sets the number of ticks the player should be frozen
     *
     * @param numberOfTicks The number of ticks
     */
    @Override
    public void setFreezeTicks(int numberOfTicks)
    {
        this.freezeTicksRemaining = numberOfTicks;
    }

    /**
     * Gets the number of ticks the player should be frozen
     *
     * @return The number of ticks
     */
    @Override
    public int getFreezeTicks()
    {
        return this.freezeTicksRemaining;
    }

    /**
     * Sets the position to freeze the player at
     *
     * @param position The position to freeze the player at
     */
    @Override
    public void setFreezePosition(Vec3d position)
    {
        this.position = position;
    }

    /**
     * Gets the position the player is frozen at
     *
     * @return The position the player is frozen at
     */
    @Override
    public Vec3d getFreezePosition()
    {
        return this.position;
    }

    /**
     * Sets the direction the player was looking when frozen
     *
     * @param yaw The yaw of the direction the player is looking
     * @param pitch The pitch of the direction the player is looking
     */
    @Override
    public void setFreezeDirection(float yaw, float pitch)
    {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * Gets the yaw of the direction the player is frozen towards
     *
     * @return The yaw that the player was looking when frozen
     */
    @Override
    public float getFreezeYaw()
    {
        return this.yaw;
    }

    /**
     * Gets the pitch of the direction the player is frozen towards
     *
     * @return The pitch that the player was looking when frozen
     */
    @Override
    public float getFreezePitch()
    {
        return this.pitch;
    }

    /**
     * Synchronizes freeze data between server and client
     *
     * @param entityPlayer The player to sync freeze data to
     */
    @Override
    public void sync(EntityPlayer entityPlayer)
    {
        // If we are on the server side sync this data to the client side
        if (isServerSide(entityPlayer))
        {
            AfraidOfTheDark.INSTANCE.getPacketHandler().sendTo(new SyncFreezeData(this.freezeTicksRemaining, this.position, this.yaw, this.pitch), (EntityPlayerMP) entityPlayer);
        }
    }
}
