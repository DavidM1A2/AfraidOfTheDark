package com.davidm1a2.afraidofthedark.common.capabilities.player.research

import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.nbt.NBTBase
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilitySerializable

/**
 * Class responsible for providing a capability to a player
 *
 * @property instance instance of the player research capability
 */
class AOTDPlayerResearchProvider : ICapabilitySerializable<NBTBase>
{
    private val instance = ModCapabilities.PLAYER_RESEARCH.defaultInstance

    /**
     * Tests if the given capability is the player research capability
     *
     * @param capability The capability to test
     * @param facing     ignored
     * @return True if the capability is a player research capability, false otherwise
     */
    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean
    {
        return capability === ModCapabilities.PLAYER_RESEARCH
    }

    /**
     * Getter for a capability
     *
     * @param capability The capability to get
     * @param facing     ignored
     * @param <T>        The type of capability
     * @return The capability or null if it was the wrong type
     */
    override fun <T> getCapability(capability: Capability<T>, facing: EnumFacing?): T?
    {
        return if (capability === ModCapabilities.PLAYER_RESEARCH) ModCapabilities.PLAYER_RESEARCH.cast(instance) else null
    }

    /**
     * Serializes this capability to NBT using the storage's write to NBT method
     *
     * @return The NBTTagCompound representing this capability
     */
    override fun serializeNBT(): NBTBase
    {
        return ModCapabilities.PLAYER_RESEARCH.storage.writeNBT(ModCapabilities.PLAYER_RESEARCH, instance, null)!!
    }

    /**
     * Deserializes this capability into the default storage using the read from NBT method
     *
     * @param nbt The NBT tag compound to read from
     */
    override fun deserializeNBT(nbt: NBTBase)
    {
        ModCapabilities.PLAYER_RESEARCH.storage.readNBT(ModCapabilities.PLAYER_RESEARCH, instance, null, nbt)
    }
}