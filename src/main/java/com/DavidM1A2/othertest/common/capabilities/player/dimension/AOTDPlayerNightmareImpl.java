package com.DavidM1A2.afraidofthedark.common.capabilities.player.dimension;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

/**
 * Default implementation of the AOTD player nightmare capability
 */
public class AOTDPlayerNightmareImpl implements IAOTDPlayerNightmareData
{
    // Int telling us what index in the void chest dimension this player owns. Initialize to -1 so that
    // We can test if the player has a position so far or not
    private int positionIndex = -1;
    // NBTTagList containing all player inventory data before teleporting to the nightmare
    private NBTTagList preTeleportInventory;
    // The position of the player before teleporting to the nightmare
    private BlockPos preTeleportPosition;
    // The dimension id of the player before teleporting to the nightmare
    private int preTeleportDimensionID;

    /**
     * @return the index corresponding to the player's void chest position in the nightmare dimension
     */
    @Override
    public int getPositionalIndex()
    {
        return this.positionIndex;
    }

    /**
     * Sets the index corresponding to the player's nightmare position in the nightmare dimension
     *
     * @param locationIndex The index to set
     */
    @Override
    public void setPositionalIndex(int locationIndex)
    {
        this.positionIndex = locationIndex;
    }

    /**
     * @return the position in world that the player was at before teleporting into the nightmare
     */
    @Override
    public BlockPos getPreTeleportPosition()
    {
        return this.preTeleportPosition;
    }

    /**
     * Sets the position in the world that the player was at before teleporting into the nightmare
     *
     * @param blockPos The new position
     */
    @Override
    public void setPreTeleportPosition(BlockPos blockPos)
    {
        this.preTeleportPosition = blockPos;
    }

    /**
     * @return The ID of the dimension the player was in before teleporting to the nightmare
     */
    @Override
    public int getPreTeleportDimensionID()
    {
        return this.preTeleportDimensionID;
    }

    /**
     * Sets the ID of the dimension the player was in before teleporting to the nightmare
     *
     * @param dimensionID The dimension ID
     */
    @Override
    public void setPreTeleportDimensionID(int dimensionID)
    {
        this.preTeleportDimensionID = dimensionID;
    }

    /**
     * Gets the player's inventory before teleporting to the nightmare
     *
     * @return The inventory NBT
     */
    @Override
    public NBTTagList getPreTeleportPlayerInventory()
    {
        return this.preTeleportInventory;
    }

    /**
     * Sets the player's inventory before teleporting to the nightmare
     *
     * @param inventory The inventory NBT
     */
    @Override
    public void setPreTeleportPlayerInventory(NBTTagList inventory)
    {
        this.preTeleportInventory = inventory;
    }
}
