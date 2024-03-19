package com.davidm1a2.afraidofthedark.common.network.dataserializer

import net.minecraft.network.PacketBuffer
import net.minecraft.network.datasync.IDataSerializer
import net.minecraft.network.syncher.EntityDataSerializer
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.phys.Vec3

class Vector3dDataSerializer : EntityDataSerializer<Vec3> {
    override fun write(buffer: PacketBuffer, vector3d: Vec3) {
        buffer.writeDouble(vector3d.x)
        buffer.writeDouble(vector3d.y)
        buffer.writeDouble(vector3d.z)
    }

    override fun read(buffer: PacketBuffer): Vec3 {
        return Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble())
    }

    override fun copy(vector3d: Vector3d): Vec3 {
        return Vec3(vector3d.x, vector3d.y, vector3d.z)
    }
}