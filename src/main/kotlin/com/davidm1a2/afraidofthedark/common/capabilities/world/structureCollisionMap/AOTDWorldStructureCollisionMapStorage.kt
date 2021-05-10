package com.davidm1a2.afraidofthedark.common.capabilities.world.structureCollisionMap

import net.minecraft.nbt.INBT
import net.minecraft.nbt.ListNBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import org.apache.commons.logging.LogFactory

/**
 * Default storage implementation for AOTD structure collision map
 */
class AOTDWorldStructureCollisionMapStorage : Capability.IStorage<IAOTDWorldStructureCollisionMap> {
    override fun writeNBT(capability: Capability<IAOTDWorldStructureCollisionMap>, instance: IAOTDWorldStructureCollisionMap, side: Direction?): INBT {
        return instance.getBoundingBoxTree().serializeNBT()
    }

    override fun readNBT(capability: Capability<IAOTDWorldStructureCollisionMap>, instance: IAOTDWorldStructureCollisionMap, side: Direction?, nbt: INBT) {
        if (nbt is ListNBT) {
            instance.getBoundingBoxTree().deserializeNBT(nbt)
        } else {
            LOG.error("Attempted to deserialize an NBTBase that was not an ListNBT")
        }
    }

    companion object {
        private val LOG = LogFactory.getLog(AOTDWorldStructureCollisionMapStorage::class.java)
    }
}