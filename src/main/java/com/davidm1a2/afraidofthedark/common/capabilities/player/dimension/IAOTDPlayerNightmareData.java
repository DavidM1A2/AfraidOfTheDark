package com.davidm1a2.afraidofthedark.common.capabilities.player.dimension;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

/**
 * An interface that is a base for AOTD player nightmare data capabilities. This specific capability is only used server side!
 * This means no packets are needed to update the player about state changes and no sync method is present
 */
public interface IAOTDPlayerNightmareData extends IAOTDIslandData
{
    /**
     * @return the index corresponding to the player's nightmare position in the nightmare dimension
     */
    int getPositionalIndex();

    /**
     * Sets the index corresponding to the player's nightmare position in the nightmare dimension
     *
     * @param locationIndex The index to set
     */
    void setPositionalIndex(int locationIndex);

    /**
     * @return the position in world that the player was at before teleporting into the nightmare
     */
    BlockPos getPreTeleportPosition();

    /**
     * Sets the position in the world that the player was at before teleporting into the nightmare
     *
     * @param blockPos The new position
     */
    void setPreTeleportPosition(BlockPos blockPos);

    /**
     * @return The ID of the dimension the player was in before teleporting to the nightmare
     */
    int getPreTeleportDimensionID();

    /**
     * Sets the ID of the dimension the player was in before teleporting to the nightmare
     *
     * @param dimensionID The dimension ID
     */
    void setPreTeleportDimensionID(int dimensionID);

    /**
     * Gets the player's inventory before teleporting to the nightmare
     *
     * @return The inventory NBT
     */
    NBTTagList getPreTeleportPlayerInventory();

    /**
     * Sets the player's inventory before teleporting to the nightmare
     *
     * @param inventory The inventory NBT
     */
    void setPreTeleportPlayerInventory(NBTTagList inventory);
}
