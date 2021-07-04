package com.davidm1a2.afraidofthedark.common.capabilities.world.structureCollisionMap

import net.minecraft.nbt.INBT
import net.minecraft.nbt.ListNBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import org.apache.logging.log4j.LogManager

/**
 * Default storage implementation for AOTD structure collision map
 */
class WorldStructureCollisionMapStorage : Capability.IStorage<IWorldStructureCollisionMap> {
    override fun writeNBT(capability: Capability<IWorldStructureCollisionMap>, instance: IWorldStructureCollisionMap, side: Direction?): INBT {
        return instance.getBoundingBoxTree().serializeNBT()
    }

    override fun readNBT(capability: Capability<IWorldStructureCollisionMap>, instance: IWorldStructureCollisionMap, side: Direction?, nbt: INBT) {
        if (nbt is ListNBT) {
            instance.getBoundingBoxTree().deserializeNBT(nbt)
        } else {
            LOG.error("Attempted to deserialize an NBTBase that was not an ListNBT")
        }
    }

    companion object {
        private val LOG = LogManager.getLogger()
    }
}