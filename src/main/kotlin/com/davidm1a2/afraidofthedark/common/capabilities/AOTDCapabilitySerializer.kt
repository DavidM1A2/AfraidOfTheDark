package com.davidm1a2.afraidofthedark.common.capabilities

import net.minecraft.core.Direction
import net.minecraft.nbt.Tag
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import org.apache.logging.log4j.LogManager

abstract class AOTDCapabilitySerializer<I : Any, T : Tag>(internal val instance: I) : INullableCapabilitySerializable<T> {
    abstract fun getCapability(): Capability<I>

    override fun <V> getCapability(capability: Capability<V>?, side: Direction?): LazyOptional<V> {
        return if (capability == getCapability()) LazyOptional.of { instance }.cast() else LazyOptional.empty()
    }

    override fun deserializeNBT(nbt: T?) {
        if (nbt == null) {
            LOG.error("Attempted to deserialize an nbt from type {} that was null!", this::class.java.simpleName)
            return
        }
        deserializeNBTSafe(nbt)
    }

    abstract fun deserializeNBTSafe(nbt: T)

    companion object {
        private val LOG = LogManager.getLogger()
    }
}