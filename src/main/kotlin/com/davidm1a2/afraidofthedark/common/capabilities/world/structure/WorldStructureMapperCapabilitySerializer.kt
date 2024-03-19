package com.davidm1a2.afraidofthedark.common.capabilities.world.structure

import com.davidm1a2.afraidofthedark.common.capabilities.INullableCapabilitySerializable
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import org.apache.logging.log4j.LogManager

class WorldStructureMapperCapabilitySerializer : INullableCapabilitySerializable<CompoundTag> {
    private val instance: IWorldStructureMapper = WorldStructureMapper()

    override fun <V> getCapability(capability: Capability<V>?, side: Direction?): LazyOptional<V> {
        return if (capability == ModCapabilities.WORLD_STRUCTURE_MAPPER) LazyOptional.of { instance }.cast() else LazyOptional.empty()
    }

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

    override fun deserializeNBT(nbt: CompoundTag?) {
        if (nbt != null) {
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