package com.davidm1a2.afraidofthedark.common.capabilities.world

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.world.aabb.BoundingBoxTree
import net.minecraft.nbt.CompoundNBT
import net.minecraft.world.IWorld
import net.minecraft.world.gen.feature.structure.StructureStart
import net.minecraft.world.server.ServerWorld
import net.minecraft.world.storage.WorldSavedData

class StructureCollisionMap @JvmOverloads constructor(identifier: String = IDENTIFIER) : WorldSavedData(identifier) {
    private val map = BoundingBoxTree()

    override fun read(nbt: CompoundNBT) {
        val mapData = nbt.getList(NBT_STRUCTURE_COLLISION_MAP, net.minecraftforge.common.util.Constants.NBT.TAG_INT)
        map.deserializeNBT(mapData)
    }

    override fun write(nbt: CompoundNBT): CompoundNBT {
        val mapData = map.serializeNBT()
        nbt.put(NBT_STRUCTURE_COLLISION_MAP, mapData)
        return nbt
    }

    fun insertStructure(start: StructureStart) {
        map.insert(start.boundingBox)
        markDirty()
    }

    fun isStructureBlocked(start: StructureStart): Boolean {
        return map.intersects(start.boundingBox)
    }

    companion object {
        private const val IDENTIFIER = Constants.MOD_ID + "_structure_collision_map"

        private const val NBT_STRUCTURE_COLLISION_MAP = "structureCollisionMap"

        fun get(world: IWorld): StructureCollisionMap {
            // If we are on client side fail
            if (world.isRemote) {
                throw UnsupportedOperationException("Attempted to get the structure collision map client side!")
            }
            val storage = (world as ServerWorld).savedData
            // func_212426_a roughly equal to getOrLoadData?
            var collisionMap = storage.get({ StructureCollisionMap() }, IDENTIFIER)
            if (collisionMap == null) {
                collisionMap = StructureCollisionMap()
                // func_212424_a roughly equal to setData?
                storage.set(collisionMap)
                collisionMap.markDirty()
            }
            // Return the data
            return collisionMap
        }
    }
}