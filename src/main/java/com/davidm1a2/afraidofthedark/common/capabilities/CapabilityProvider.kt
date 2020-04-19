package com.davidm1a2.afraidofthedark.common.capabilities

import net.minecraft.nbt.NBTBase
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilitySerializable

/**
 * Class responsible for providing a capability
 *
 * @property capability The capability
 * @property capabilityInstance The default instance of the capability
 */
class CapabilityProvider<T>(private val capability: Capability<T>) : ICapabilitySerializable<NBTBase> {
    private val capabilityInstance = capability.defaultInstance

    /**
     * Tests if the given capability is the right one
     *
     * @param capability The capability to test
     * @param facing     ignored
     * @return True if the capability is the right one, false otherwise
     */
    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        return capability == this.capability
    }

    /**
     * Getter for a capability
     *
     * @param capability The capability to get
     * @param facing     ignored
     * @param <T>        The type of capability
     * @return The capability or null if it was the wrong type
     */
    override fun <V> getCapability(capability: Capability<V>, facing: EnumFacing?): V? {
        return if (capability == this.capability) this.capability.cast(capabilityInstance) else null
    }

    /**
     * Serializes this capability to NBT using the storage's write to NBT method
     *
     * @return The NBTTagCompound representing this capability
     */
    override fun serializeNBT(): NBTBase {
        return this.capability.storage.writeNBT(this.capability, this.capabilityInstance, null)!!
    }

    /**
     * Deserializes this capability into the default storage using the read from NBT method
     *
     * @param nbt The NBT tag compound to read from
     */
    override fun deserializeNBT(nbt: NBTBase) {
        this.capability.storage.readNBT(this.capability, this.capabilityInstance, null, nbt)
    }
}