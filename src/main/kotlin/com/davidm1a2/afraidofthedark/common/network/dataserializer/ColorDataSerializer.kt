package com.davidm1a2.afraidofthedark.common.network.dataserializer

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.syncher.EntityDataSerializer
import java.awt.Color

class ColorDataSerializer : EntityDataSerializer<Color> {
    override fun write(buffer: FriendlyByteBuf, color: Color) {
        buffer.writeInt(color.red)
        buffer.writeInt(color.green)
        buffer.writeInt(color.blue)
    }

    override fun read(buffer: FriendlyByteBuf): Color {
        return Color(buffer.readInt(), buffer.readInt(), buffer.readInt())
    }

    override fun copy(color: Color): Color {
        return Color(color.red, color.green, color.blue)
    }
}