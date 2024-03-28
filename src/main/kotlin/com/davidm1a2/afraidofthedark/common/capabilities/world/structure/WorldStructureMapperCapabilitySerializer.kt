package com.davidm1a2.afraidofthedark.common.capabilities.world.structure

import com.davidm1a2.afraidofthedark.common.capabilities.AOTDCapabilitySerializer
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag

class WorldStructureMapperCapabilitySerializer(instance: IWorldStructureMapper = WorldStructureMapper()) : AOTDCapabilitySerializer<IWorldStructureMapper, CompoundTag>(instance) {
    override fun getCapability() = ModCapabilities.WORLD_STRUCTURE_MAPPER

    override fun serializeNBT(): CompoundTag {
        val nbt = CompoundTag()

        // Store structure maps
        val structureMaps = instance.getStructureMaps()
        val structureMapCoords = IntArray(structureMaps.size * 2)
        val structureMapData = ListTag()
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

    override fun deserializeNBTSafe(nbt: CompoundTag) {
        // Read structure maps
        val structureMapCoords = nbt.getIntArray(NBT_STRUCTURE_MAP_COORDS_NBT)
        val structureMapData = nbt.getList(NBT_STRUCTURE_MAP_DATA_NBT, Tag.TAG_COMPOUND.toInt())
        val structureMaps = mutableListOf<Pair<StructureGridPos, StructureMap>>()
        for (i in structureMapCoords.indices step 2) {
            val chunkPos = StructureGridPos(structureMapCoords[i], structureMapCoords[i + 1], StructureGridSize.LARGEST_GRID_SIZE)
            val structureMap = StructureMap().apply { deserializeNBT(structureMapData.getCompound(i / 2)) }
            structureMaps.add(chunkPos to structureMap)
        }
        instance.setStructureMaps(structureMaps)
    }

    companion object {
        private const val NBT_STRUCTURE_MAP_COORDS_NBT = "structure_map_coords"
        private const val NBT_STRUCTURE_MAP_DATA_NBT = "structure_map_data"
    }
}