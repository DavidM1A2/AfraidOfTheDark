package com.davidm1a2.afraidofthedark.common.capabilities

import net.minecraft.nbt.INBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import net.minecraftforge.common.util.LazyOptional

/**
 * Class responsible for providing a capability
 *
 * @property capability The capability
 * @property capabilityInstance The default instance of the capability
 */
class CapabilityProvider<T>(private val capability: Capability<T>) : ICapabilitySerializable<INBT> {
    private val capabilityInstance = capability.defaultInstance

    /**
     * Getter for a capability
     *
     * @param capability The capability to get
     * @param facing     ignored
     * @param <T>        The type of capability
     * @return The capability or null if it was the wrong type
     */
    override fun <V> getCapability(capability: Capability<V>, facing: Direction?): LazyOptional<V> {
        @Suppress("NULLABLE_TYPE_PARAMETER_AGAINST_NOT_NULL_TYPE_PARAMETER")
        val instance = capabilityInstance?.let { LazyOptional.of { it } } ?: LazyOptional.empty()
        return if (capability == this.capability) instance.cast() else LazyOptional.empty()
    }

    /**
     * Serializes this capability to NBT using the storage's write to NBT method
     *
     * @return The NBTTagCompound representing this capability
     */
    override fun serializeNBT(): INBT {
        return this.capability.storage.writeNBT(this.capability, this.capabilityInstance, null)!!
    }

    /**
     * Deserializes this capability into the default storage using the read from NBT method
     *
     * @param nbt The NBT tag compound to read from
     */
    override fun deserializeNBT(nbt: INBT) {
        this.capability.storage.readNBT(this.capability, this.capabilityInstance, null, nbt)
    }
}