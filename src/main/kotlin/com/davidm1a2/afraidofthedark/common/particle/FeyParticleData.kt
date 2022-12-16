package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.mojang.brigadier.StringReader
import com.mojang.datafixers.util.Function4
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.PacketBuffer
import net.minecraft.particles.IParticleData
import net.minecraft.particles.ParticleType

class FeyParticleData(val scale: Float, val red: Float, val green: Float, val blue: Float) : IParticleData {
    override fun getType(): ParticleType<*> {
        return ModParticles.FEY
    }

    override fun writeToNetwork(packetBuffer: PacketBuffer) {
        packetBuffer.writeFloat(scale)
        packetBuffer.writeFloat(red)
        packetBuffer.writeFloat(green)
        packetBuffer.writeFloat(blue)
    }

    override fun writeToString(): String {
        return "$scale, $red, $green, $blue"
    }

    companion object {
        val CODEC: Codec<FeyParticleData> = RecordCodecBuilder.create {
            it.group(
                Codec.FLOAT.fieldOf("scale").forGetter(FeyParticleData::scale),
                Codec.FLOAT.fieldOf("red").forGetter(FeyParticleData::red),
                Codec.FLOAT.fieldOf("green").forGetter(FeyParticleData::green),
                Codec.FLOAT.fieldOf("blue").forGetter(FeyParticleData::blue)
            ).apply(it, it.stable(Function4 { scale, red, green, blue ->
                FeyParticleData(scale, red, green, blue)
            }))
        }

        val DESERIALIZER = object : IParticleData.IDeserializer<FeyParticleData> {
            override fun fromCommand(particleType: ParticleType<FeyParticleData>, stringReader: StringReader): FeyParticleData {
                return FeyParticleData(stringReader.readFloat(), stringReader.readFloat(), stringReader.readFloat(), stringReader.readFloat())
            }

            override fun fromNetwork(particleType: ParticleType<FeyParticleData>, packetBuffer: PacketBuffer): FeyParticleData {
                return FeyParticleData(packetBuffer.readFloat(), packetBuffer.readFloat(), packetBuffer.readFloat(), packetBuffer.readFloat())
            }
        }
    }
}