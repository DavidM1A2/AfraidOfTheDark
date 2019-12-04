package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.Vec3d
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage

/**
 * Default storage implementation for the AOTD freeze spell effect data
 */
class AOTDPlayerSpellFreezeDataStorage : IStorage<IAOTDPlayerSpellFreezeData>
{
    /**
     * Called to write a capability to an NBT compound
     *
     * @param capability The capability that is being written
     * @param instance   The instance to of the capability to write
     * @param side       ignored
     * @return An NBTTagCompound that contains all info about the capability
     */
    override fun writeNBT(capability: Capability<IAOTDPlayerSpellFreezeData>,
                          instance: IAOTDPlayerSpellFreezeData,
                          side: EnumFacing?): NBTBase?
    {
        // Create a compound to write
        val nbt = NBTTagCompound()
        nbt.setInteger(NBT_FREEZE_TICKS, instance.freezeTicks)
        if (instance.freezePosition != null)
        {
            nbt.setDouble(NBT_POSITION + "_x", instance.freezePosition!!.x)
            nbt.setDouble(NBT_POSITION + "_y", instance.freezePosition!!.y)
            nbt.setDouble(NBT_POSITION + "_z", instance.freezePosition!!.z)
        }
        nbt.setFloat(NBT_DIRECTION_YAW, instance.getFreezeYaw())
        nbt.setFloat(NBT_DIRECTION_PITCH, instance.getFreezePitch())
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
    override fun readNBT(capability: Capability<IAOTDPlayerSpellFreezeData>,
                         instance: IAOTDPlayerSpellFreezeData,
                         side: EnumFacing?,
                         nbt: NBTBase)
    {
        // Test if the nbt tag base is an NBT tag compound
        if (nbt is NBTTagCompound)
        {
            instance.freezeTicks = nbt.getInteger(NBT_FREEZE_TICKS)
            if (nbt.hasKey(NBT_POSITION + "_x") && nbt.hasKey(NBT_POSITION + "_y") && nbt.hasKey(
                            NBT_POSITION + "_z"))
            {
                instance.freezePosition = Vec3d(
                        nbt.getDouble(NBT_POSITION + "_x"),
                        nbt.getDouble(NBT_POSITION + "_y"),
                        nbt.getDouble(NBT_POSITION + "_z"))
            }
            instance.setFreezeDirection(nbt.getFloat(NBT_DIRECTION_YAW),
                    nbt.getFloat(NBT_DIRECTION_PITCH))
        }
        else
        {
            AfraidOfTheDark.INSTANCE.logger.error("Attempted to deserialize an NBTBase that was not an NBTTagCompound!")
        }
    }

    companion object
    {
        // NBT constants used for serialization
        private const val NBT_FREEZE_TICKS = "freeze_ticks"
        private const val NBT_POSITION = "position"
        private const val NBT_DIRECTION_YAW = "direction_yaw"
        private const val NBT_DIRECTION_PITCH = "direction_pitch"
    }
}