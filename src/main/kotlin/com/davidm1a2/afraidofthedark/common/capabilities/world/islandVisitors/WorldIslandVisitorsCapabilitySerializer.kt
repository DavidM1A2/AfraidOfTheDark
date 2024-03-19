package com.davidm1a2.afraidofthedark.common.capabilities.world.islandVisitors

import com.davidm1a2.afraidofthedark.common.capabilities.INullableCapabilitySerializable
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.core.Direction
import net.minecraft.nbt.IntTag
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import org.apache.logging.log4j.LogManager

class WorldIslandVisitorsCapabilitySerializer : INullableCapabilitySerializable<IntTag> {
    private val instance: IWorldIslandVisitors = WorldIslandVisitors()

    override fun <V> getCapability(capability: Capability<V>?, side: Direction?): LazyOptional<V> {
        return if (capability == ModCapabilities.WORLD_ISLAND_VISITORS) LazyOptional.of { instance }.cast() else LazyOptional.empty()
    }

    override fun serializeNBT(): IntTag {
        return IntTag.valueOf(instance.getNumberOfVisitors())
    }

    override fun deserializeNBT(nbt: IntTag?) {
        if (nbt is IntTag) {
            instance.setNumberOfVisitors(nbt.asInt)
        } else {
            LOG.error("Attempted to deserialize an NBTBase that was null!")
        }
    }

    companion object {
        private val LOG = LogManager.getLogger()
    }
}