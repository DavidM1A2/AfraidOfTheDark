package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.util.Direction
import net.minecraft.util.math.Vec3d
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage
import org.apache.logging.log4j.LogManager

/**
 * Default storage implementation for the AOTD freeze spell effect data
 */
class PlayerSpellFreezeDataStorage : IStorage<IPlayerSpellFreezeData> {
    /**
     * Called to write a capability to an NBT compound
     *
     * @param capability The capability that is being written
     * @param instance   The instance to of the capability to write
     * @param side       ignored
     * @return An NBTTagCompound that contains all info about the capability
     */
    override fun writeNBT(
        capability: Capability<IPlayerSpellFreezeData>,
        instance: IPlayerSpellFreezeData,
        side: Direction?
    ): INBT {
        // Create a compound to write
        val nbt = CompoundNBT()
        nbt.putInt(NBT_FREEZE_TICKS, instance.freezeTicks)
        instance.freezePosition?.let {
            nbt.putDouble(NBT_POSITION + "_x", it.x)
            nbt.putDouble(NBT_POSITION + "_y", it.y)
            nbt.putDouble(NBT_POSITION + "_z", it.z)
        }
        nbt.putFloat(NBT_DIRECTION_YAW, instance.getFreezeYaw())
        nbt.putFloat(NBT_DIRECTION_PITCH, instance.getFreezePitch())
        return nbt
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
        capability: Capability<IPlayerSpellFreezeData>,
        instance: IPlayerSpellFreezeData,
        side: Direction?,
        nbt: INBT
    ) {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt is CompoundNBT) {
            instance.freezeTicks = nbt.getInt(NBT_FREEZE_TICKS)

            if (nbt.contains(NBT_POSITION + "_x") &&
                nbt.contains(NBT_POSITION + "_y") &&
                nbt.contains(NBT_POSITION + "_z")
            ) {
                instance.freezePosition = Vec3d(
                    nbt.getDouble(NBT_POSITION + "_x"),
                    nbt.getDouble(NBT_POSITION + "_y"),
                    nbt.getDouble(NBT_POSITION + "_z")
                )
            }

            instance.setFreezeDirection(
                nbt.getFloat(NBT_DIRECTION_YAW),
                nbt.getFloat(NBT_DIRECTION_PITCH)
            )
        } else {
            logger.error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!")
        }
    }

    companion object {
        private val logger = LogManager.getLogger()

        // NBT constants used for serialization
        private const val NBT_FREEZE_TICKS = "freeze_ticks"
        private const val NBT_POSITION = "position"
        private const val NBT_DIRECTION_YAW = "direction_yaw"
        private const val NBT_DIRECTION_PITCH = "direction_pitch"
    }
}