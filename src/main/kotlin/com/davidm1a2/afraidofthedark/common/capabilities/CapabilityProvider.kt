package com.davidm1a2.afraidofthedark.common.capabilities

import net.minecraft.nbt.INBTBase
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import net.minecraftforge.common.util.LazyOptional

/**
 * Class responsible for providing a capability
 *
 * @property capability The capability
 * @property capabilityInstance The default instance of the capability
 */
class CapabilityProvider<T>(private val capability: Capability<T>) : ICapabilitySerializable<INBTBase> {
    private val capabilityInstance = capability.defaultInstance

    /**
     * Getter for a capability
     *
     * @param capability The capability to get
     * @param facing     ignored
     * @param <T>        The type of capability
     * @return The capability or null if it was the wrong type
     */
    override fun <V> getCapability(capability: Capability<V>, facing: EnumFacing?): LazyOptional<V> {
        val instance = capabilityInstance?.let { LazyOptional.of { it } } ?: LazyOptional.empty()
        return if (capability == this.capability) instance.cast() else LazyOptional.empty()
    }

    /**
     * Serializes this capability to NBT using the storage's write to NBT method
     *
     * @return The NBTTagCompound representing this capability
     */
    override fun serializeNBT(): INBTBase {
        return this.capability.storage.writeNBT(this.capability, this.capabilityInstance, null)!!
    }

    /**
     * Deserializes this capability into the default storage using the read from NBT method
     *
     * @param nbt The NBT tag compound to read from
     */
    override fun deserializeNBT(nbt: INBTBase) {
        this.capability.storage.readNBT(this.capability, this.capabilityInstance, null, nbt)
    }
}