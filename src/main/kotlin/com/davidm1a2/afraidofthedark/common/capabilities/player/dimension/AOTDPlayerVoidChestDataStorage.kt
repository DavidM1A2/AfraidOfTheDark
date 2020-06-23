package com.davidm1a2.afraidofthedark.common.capabilities.player.dimension

import net.minecraft.nbt.INBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage
import org.apache.logging.log4j.LogManager

/**
 * Default storage implementation for AOTD void chest data
 */
class AOTDPlayerVoidChestDataStorage : IStorage<IAOTDPlayerVoidChestData> {
    /**
     * Called to write a capability to an NBT compound
     *
     * @param capability The capability that is being written
     * @param instance   The instance to of the capability to write
     * @param side       ignored
     * @return An NBTTagCompound that contains all info about the capability
     */
    override fun writeNBT(
        capability: Capability<IAOTDPlayerVoidChestData>,
        instance: IAOTDPlayerVoidChestData,
        side: EnumFacing?
    ): INBTBase {
        // Create a compound to write
        val compound = NBTTagCompound()
        compound.setInt(NBT_POSITIONAL_INDEX, instance.positionalIndex)
        compound.setInt(NBT_FRIENDS_INDEX, instance.friendsIndex)
        instance.preTeleportPosition?.let { compound.setTag(NBT_PRE_TELEPORT_POSITION, NBTUtil.writeBlockPos(it)) }
        instance.preTeleportDimension?.let { compound.setString(NBT_PRE_TELEPORT_DIMENSION, DimensionType.func_212678_a(it).toString()) }
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
    override fun readNBT(
        capability: Capability<IAOTDPlayerVoidChestData>,
        instance: IAOTDPlayerVoidChestData,
        side: EnumFacing?,
        nbt: INBTBase
    ) {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt is NBTTagCompound) {
            // The compound to read from
            instance.positionalIndex = nbt.getInt(NBT_POSITIONAL_INDEX)
            instance.friendsIndex = nbt.getInt(NBT_FRIENDS_INDEX)

            if (nbt.hasKey(NBT_PRE_TELEPORT_POSITION)) {
                instance.preTeleportPosition =
                    NBTUtil.readBlockPos(nbt.getTag(NBT_PRE_TELEPORT_POSITION) as NBTTagCompound)
            } else {
                instance.preTeleportPosition = null
            }

            if (nbt.hasKey(NBT_PRE_TELEPORT_DIMENSION)) {
                instance.preTeleportDimension = DimensionType.byName(ResourceLocation(nbt.getString(NBT_PRE_TELEPORT_DIMENSION)))
            } else {
                instance.preTeleportDimension = null
            }
        } else {
            logger.error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!")
        }
    }

    companion object {
        private val logger = LogManager.getLogger()

        // Constant IDs used in NBT
        private const val NBT_POSITIONAL_INDEX = "positional_index"
        private const val NBT_FRIENDS_INDEX = "friends_index"
        private const val NBT_PRE_TELEPORT_POSITION = "pre_teleport_position"
        private const val NBT_PRE_TELEPORT_DIMENSION = "pre_teleport_dimension"
    }
}