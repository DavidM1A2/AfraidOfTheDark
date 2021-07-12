package com.davidm1a2.afraidofthedark.common.capabilities.world.islandVisitors

import net.minecraft.nbt.INBT
import net.minecraft.nbt.IntNBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import org.apache.logging.log4j.LogManager

/**
 * Default storage implementation for AOTD island visitors
 */
class WorldIslandVisitorsStorage : Capability.IStorage<IWorldIslandVisitors> {
    override fun writeNBT(capability: Capability<IWorldIslandVisitors>, instance: IWorldIslandVisitors, side: Direction?): INBT {
        return IntNBT.valueOf(instance.getNumberOfVisitors())
    }

    override fun readNBT(capability: Capability<IWorldIslandVisitors>, instance: IWorldIslandVisitors, side: Direction?, nbt: INBT) {
        if (nbt is IntNBT) {
            instance.setNumberOfVisitors(nbt.int)
        } else {
            LOG.error("Attempted to deserialize an NBTBase that was not an IntNBT")
        }
    }

    companion object {
        private val LOG = LogManager.getLogger()
    }
}