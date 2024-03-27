package com.davidm1a2.afraidofthedark.common.network.dataserializer

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.syncher.EntityDataSerializer
import net.minecraft.world.phys.Vec3

class Vector3dDataSerializer : EntityDataSerializer<Vec3> {
    override fun write(buffer: FriendlyByteBuf, vector3d: Vec3) {
        buffer.writeDouble(vector3d.x)
        buffer.writeDouble(vector3d.y)
        buffer.writeDouble(vector3d.z)
    }

    override fun read(buffer: FriendlyByteBuf): Vec3 {
        return Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble())
    }

    override fun copy(vector3d: Vec3): Vec3 {
        return Vec3(vector3d.x, vector3d.y, vector3d.z)
    }
}