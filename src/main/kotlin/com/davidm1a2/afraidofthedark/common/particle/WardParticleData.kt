package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.mojang.brigadier.StringReader
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.FriendlyByteBuf
import java.util.function.BiFunction

class WardParticleData(val direction: Direction, val scale: Float = 2.5f) : ParticleOptions {
    override fun getType(): ParticleType<*> {
        return ModParticles.WARD
    }

    override fun writeToNetwork(packetBuffer: FriendlyByteBuf) {
        packetBuffer.writeVarInt(direction.ordinal)
        packetBuffer.writeFloat(scale)
    }

    override fun writeToString(): String {
        return direction.ordinal.toString()
    }

    companion object {
        val CODEC: Codec<WardParticleData> = RecordCodecBuilder.create {
            it.group(
                Codec.INT.fieldOf("direction").xmap({ int -> Direction.values()[int] }, { dir -> dir.ordinal }).forGetter(WardParticleData::direction),
                Codec.FLOAT.fieldOf("scale").forGetter(WardParticleData::scale)
            ).apply(it, it.stable(BiFunction { direction, scale ->
                WardParticleData(direction, scale)
            }))
        }

        val DESERIALIZER = object : ParticleOptions.Deserializer<WardParticleData> {
            override fun fromCommand(particleType: ParticleType<WardParticleData>, stringReader: StringReader): WardParticleData {
                return WardParticleData(Direction.UP)
            }

            override fun fromNetwork(particleType: ParticleType<WardParticleData>, packetBuffer: FriendlyByteBuf): WardParticleData {
                return WardParticleData(Direction.values()[packetBuffer.readVarInt()], packetBuffer.readFloat())
            }
        }
    }
}