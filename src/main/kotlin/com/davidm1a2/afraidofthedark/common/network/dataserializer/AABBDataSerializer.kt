package com.davidm1a2.afraidofthedark.common.network.dataserializer

import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraft.network.datasync.IDataSerializer
import net.minecraft.util.math.AxisAlignedBB

class AABBDataSerializer : IDataSerializer<AxisAlignedBB> {
    override fun write(buffer: PacketBuffer, aabb: AxisAlignedBB) {
        val values = CompoundNBT()
        values.putDouble("minX", aabb.minX)
        values.putDouble("minY", aabb.minY)
        values.putDouble("minZ", aabb.minZ)
        values.putDouble("maxX", aabb.maxX)
        values.putDouble("maxY", aabb.maxY)
        values.putDouble("maxZ", aabb.maxZ)
        buffer.writeCompoundTag(values)
    }

    override fun read(buffer: PacketBuffer): AxisAlignedBB {
        val values = buffer.readCompoundTag()!!
        val minX = values.getDouble("minX")
        val minY = values.getDouble("minY")
        val minZ = values.getDouble("minZ")
        val maxX = values.getDouble("maxX")
        val maxY = values.getDouble("maxY")
        val maxZ = values.getDouble("maxZ")
        return AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ)
    }

    override fun copyValue(aabb: AxisAlignedBB): AxisAlignedBB {
        return AxisAlignedBB(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ)
    }
}