package com.davidm1a2.afraidofthedark.common.capabilities.world.islandVisitors

import com.davidm1a2.afraidofthedark.common.capabilities.AOTDCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.nbt.IntTag

class WorldIslandVisitorsCapabilitySerializer(instance: IWorldIslandVisitors = WorldIslandVisitors()) : AOTDCapabilitySerializer<IWorldIslandVisitors, IntTag>(instance) {
    override fun getCapability() = ModCapabilities.WORLD_ISLAND_VISITORS

    override fun serializeNBT(): IntTag {
        return IntTag.valueOf(instance.getNumberOfVisitors())
    }

    override fun deserializeNBTSafe(nbt: IntTag) {
        instance.setNumberOfVisitors(nbt.asInt)
    }
}