package com.davidm1a2.afraidofthedark.common.capabilities.world.structureMissCounter

import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.nbt.ListNBT
import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.registries.ForgeRegistries
import org.apache.commons.logging.LogFactory

/**
 * Default storage implementation for structure miss counters
 */
class WorldStructureMissCounterStorage : Capability.IStorage<IWorldStructureMissCounter> {
    private val structures = ForgeRegistries.FEATURES.filterIsInstance<AOTDStructure<*>>()

    override fun writeNBT(capability: Capability<IWorldStructureMissCounter>, instance: IWorldStructureMissCounter, side: Direction?): INBT {
        val storageCounters = ListNBT()
        for (structure in structures) {
            val structureNbt = CompoundNBT()
            structureNbt.putString(NBT_NAME, structure.registryName.toString())
            structureNbt.putInt(NBT_COUNT, instance.get(structure))
            storageCounters.add(structureNbt)
        }
        return storageCounters
    }

    override fun readNBT(capability: Capability<IWorldStructureMissCounter>, instance: IWorldStructureMissCounter, side: Direction?, nbt: INBT) {
        if (nbt is ListNBT) {
            for (i in 0 until nbt.size) {
                val structureNbt = nbt.getCompound(i)
                val structureLocation = ResourceLocation(structureNbt.getString(NBT_NAME))
                val structure = ForgeRegistries.FEATURES.getValue(structureLocation) as AOTDStructure<*>
                instance.reset(structure)
                instance.increment(structure, structureNbt.getInt(NBT_COUNT))
            }
        } else {
            LOG.error("Attempted to deserialize an NBTBase that was not a ListNBT")
        }
    }

    companion object {
        private val LOG = LogFactory.getLog(WorldStructureMissCounterStorage::class.java)

        private const val NBT_NAME = "name"
        private const val NBT_COUNT = "count"
    }
}