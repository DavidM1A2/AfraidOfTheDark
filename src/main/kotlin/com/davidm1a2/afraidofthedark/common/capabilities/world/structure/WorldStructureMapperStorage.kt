package com.davidm1a2.afraidofthedark.common.capabilities.world.structure

import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.nbt.ListNBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.Constants
import org.apache.logging.log4j.LogManager

class WorldStructureMapperStorage : Capability.IStorage<IWorldStructureMapper> {
    override fun writeNBT(capability: Capability<IWorldStructureMapper>, instance: IWorldStructureMapper, side: Direction?): INBT {
        val nbt = CompoundNBT()

        // Store structure maps
        val structureMaps = instance.getStructureMaps()
        val structureMapCoords = IntArray(structureMaps.size * 2)
        val structureMapData = ListNBT()
        var i = 0
        structureMaps.forEach {
            structureMapCoords[i++] = it.first.x
            structureMapCoords[i++] = it.first.z
            structureMapData.add(it.second.serializeNBT())
        }
        nbt.putIntArray(NBT_STRUCTURE_MAP_COORDS_NBT, structureMapCoords)
        nbt.put(NBT_STRUCTURE_MAP_DATA_NBT, structureMapData)

        return nbt
    }

    override fun readNBT(capability: Capability<IWorldStructureMapper>, instance: IWorldStructureMapper, side: Direction?, nbt: INBT) {
        if (nbt is CompoundNBT) {
            // Read structure maps
            val structureMapCoords = nbt.getIntArray(NBT_STRUCTURE_MAP_COORDS_NBT)
            val structureMapData = nbt.getList(NBT_STRUCTURE_MAP_DATA_NBT, Constants.NBT.TAG_COMPOUND)
            val structureMaps = mutableListOf<Pair<StructureGridPos, StructureMap>>()
            for (i in structureMapCoords.indices step 2) {
                val chunkPos = StructureGridPos(structureMapCoords[i], structureMapCoords[i + 1], StructureGridSize.LARGEST_GRID_SIZE)
                val structureMap = StructureMap().apply { deserializeNBT(structureMapData.getCompound(i / 2)) }
                structureMaps.add(chunkPos to structureMap)
            }
            instance.setStructureMaps(structureMaps)
        } else {
            LOG.error("Attempted to deserialize an NBTBase that was not a CompoundNBT")
        }
    }

    companion object {
        private const val NBT_STRUCTURE_MAP_COORDS_NBT = "structure_map_coords"
        private const val NBT_STRUCTURE_MAP_DATA_NBT = "structure_map_data"

        private val LOG = LogManager.getLogger()
    }
}