package com.davidm1a2.afraidofthedark.common.network.dataserializer

import net.minecraft.network.PacketBuffer
import net.minecraft.network.datasync.IDataSerializer
import java.awt.Color

class ColorDataSerializer : IDataSerializer<Color> {
    override fun write(buffer: PacketBuffer, color: Color) {
        buffer.writeInt(color.red)
        buffer.writeInt(color.green)
        buffer.writeInt(color.blue)
    }

    override fun read(buffer: PacketBuffer): Color {
        return Color(buffer.readInt(), buffer.readInt(), buffer.readInt())
    }

    override fun copy(color: Color): Color {
        return Color(color.red, color.green, color.blue)
    }
}