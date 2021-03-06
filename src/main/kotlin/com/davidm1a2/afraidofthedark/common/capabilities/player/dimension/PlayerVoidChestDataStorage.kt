package com.davidm1a2.afraidofthedark.common.capabilities.player.dimension

import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage
import org.apache.logging.log4j.LogManager

/**
 * Default storage implementation for AOTD void chest data
 */
class PlayerVoidChestDataStorage : IStorage<IPlayerVoidChestData> {
    /**
     * Called to write a capability to an NBT compound
     *
     * @param capability The capability that is being written
     * @param instance   The instance to of the capability to write
     * @param side       ignored
     * @return An NBTTagCompound that contains all info about the capability
     */
    override fun writeNBT(
        capability: Capability<IPlayerVoidChestData>,
        instance: IPlayerVoidChestData,
        side: Direction?
    ): INBT {
        // Create a compound to write
        val compound = CompoundNBT()
        compound.putInt(NBT_POSITIONAL_INDEX, instance.positionalIndex)
        compound.putInt(NBT_FRIENDS_INDEX, instance.friendsIndex)
        instance.preTeleportPosition?.let { compound.put(NBT_PRE_TELEPORT_POSITION, NBTUtil.writeBlockPos(it)) }
        instance.preTeleportDimension?.let { compound.putString(NBT_PRE_TELEPORT_DIMENSION, DimensionType.getKey(it).toString()) }
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
        capability: Capability<IPlayerVoidChestData>,
        instance: IPlayerVoidChestData,
        side: Direction?,
        nbt: INBT
    ) {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt is CompoundNBT) {
            // The compound to read from
            instance.positionalIndex = nbt.getInt(NBT_POSITIONAL_INDEX)
            instance.friendsIndex = nbt.getInt(NBT_FRIENDS_INDEX)

            if (nbt.contains(NBT_PRE_TELEPORT_POSITION)) {
                instance.preTeleportPosition =
                    NBTUtil.readBlockPos(nbt.getCompound(NBT_PRE_TELEPORT_POSITION))
            } else {
                instance.preTeleportPosition = null
            }

            if (nbt.contains(NBT_PRE_TELEPORT_DIMENSION)) {
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