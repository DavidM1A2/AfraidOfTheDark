package com.davidm1a2.afraidofthedark.common.capabilities.player.dimension

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage
import net.minecraftforge.common.util.Constants

/**
 * Default storage implementation for AOTD nightmare data
 */
class AOTDPlayerNightmareDataStorage : IStorage<IAOTDPlayerNightmareData>
{
    /**
     * Called to write a capability to an NBT compound
     *
     * @param capability The capability that is being written
     * @param instance   The instance to of the capability to write
     * @param side       ignored
     * @return An NBTTagCompound that contains all info about the capability
     */
    override fun writeNBT(capability: Capability<IAOTDPlayerNightmareData>,
                          instance: IAOTDPlayerNightmareData,
                          side: EnumFacing?): NBTBase?
    {
        // Create a compound to write
        val compound = NBTTagCompound()
        compound.setInteger(NBT_POSITIONAL_INDEX, instance.positionalIndex)
        if (instance.preTeleportPlayerInventory != null)
        {
            compound.setTag(NBT_PRE_TELEPORT_INVENTORY, instance.preTeleportPlayerInventory!!)
        }
        if (instance.preTeleportPosition != null)
        {
            compound.setTag(NBT_PRE_TELEPORT_POSITION, NBTUtil.createPosTag(instance.preTeleportPosition!!))
        }
        compound.setInteger(NBT_PRE_TELEPORT_DIMENSION_ID, instance.preTeleportDimensionID)
        return compound
    }

    /**
     * Called to read the NBTTagCompound into a capability
     *
     * @param capability The capability that is being read
     * @param instance   The instance to of the capability to read
     * @param side       ignored
     * @param nbt        An NBTTagCompound that contains all info about the capability
     */
    override fun readNBT(capability: Capability<IAOTDPlayerNightmareData>,
                         instance: IAOTDPlayerNightmareData,
                         side: EnumFacing?,
                         nbt: NBTBase)
    {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt is NBTTagCompound)
        {
            // The compound to read from
            instance.positionalIndex = nbt.getInteger(NBT_POSITIONAL_INDEX)
            if (nbt.hasKey(NBT_PRE_TELEPORT_INVENTORY))
            {
                instance.preTeleportPlayerInventory = nbt.getTagList(NBT_PRE_TELEPORT_INVENTORY,
                        Constants.NBT.TAG_COMPOUND)
            }
            else
            {
                instance.preTeleportPlayerInventory = null
            }
            if (nbt.hasKey(NBT_PRE_TELEPORT_POSITION))
            {
                instance.preTeleportPosition = NBTUtil.getPosFromTag(nbt.getTag(NBT_PRE_TELEPORT_POSITION) as NBTTagCompound)
            }
            else
            {
                instance.preTeleportPosition = null
            }
            instance.preTeleportDimensionID = nbt.getInteger(NBT_PRE_TELEPORT_DIMENSION_ID)
        }
        else
        {
            AfraidOfTheDark.INSTANCE.logger.error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!")
        }
    }

    companion object
    {
        // Constant IDs used in NBT
        private const val NBT_POSITIONAL_INDEX = "positional_index"
        private const val NBT_PRE_TELEPORT_INVENTORY = "pre_teleport_inventory"
        private const val NBT_PRE_TELEPORT_POSITION = "pre_teleport_position"
        private const val NBT_PRE_TELEPORT_DIMENSION_ID = "pre_teleport_dimension_id"
    }
}