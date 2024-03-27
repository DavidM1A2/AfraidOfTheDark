package com.davidm1a2.afraidofthedark.common.network.dataserializer

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.syncher.EntityDataSerializer
import net.minecraft.world.phys.AABB

class AABBDataSerializer : EntityDataSerializer<AABB> {
    override fun write(buffer: FriendlyByteBuf, aabb: AABB) {
        val values = CompoundTag()
        values.putDouble("minX", aabb.minX)
        values.putDouble("minY", aabb.minY)
        values.putDouble("minZ", aabb.minZ)
        values.putDouble("maxX", aabb.maxX)
        values.putDouble("maxY", aabb.maxY)
        values.putDouble("maxZ", aabb.maxZ)
        buffer.writeNbt(values)
    }

    override fun read(buffer: FriendlyByteBuf): AABB {
        val values = buffer.readNbt()!!
        val minX = values.getDouble("minX")
        val minY = values.getDouble("minY")
        val minZ = values.getDouble("minZ")
        val maxX = values.getDouble("maxX")
        val maxY = values.getDouble("maxY")
        val maxZ = values.getDouble("maxZ")
        return AABB(minX, minY, minZ, maxX, maxY, maxZ)
    }

    override fun copy(aabb: AABB): AABB {
        return AABB(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ)
    }
}