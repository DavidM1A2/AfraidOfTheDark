package com.davidm1a2.afraidofthedark.common.network.dataserializer

import net.minecraft.network.PacketBuffer
import net.minecraft.network.datasync.IDataSerializer
import net.minecraft.util.math.vector.Vector3d

class Vector3dDataSerializer : IDataSerializer<Vector3d> {
    override fun write(buffer: PacketBuffer, vector3d: Vector3d) {
        buffer.writeDouble(vector3d.x)
        buffer.writeDouble(vector3d.y)
        buffer.writeDouble(vector3d.z)
    }

    override fun read(buffer: PacketBuffer): Vector3d {
        return Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble())
    }

    override fun copy(vector3d: Vector3d): Vector3d {
        return Vector3d(vector3d.x, vector3d.y, vector3d.z)
    }
}