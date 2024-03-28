package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.mojang.brigadier.StringReader
import com.mojang.datafixers.util.Function4
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.FriendlyByteBuf

class FeyParticleData(val offsetDegrees: Float, val red: Float, val green: Float, val blue: Float) : ParticleOptions {
    override fun getType(): ParticleType<*> {
        return ModParticles.FEY
    }

    override fun writeToNetwork(packetBuffer: FriendlyByteBuf) {
        packetBuffer.writeFloat(offsetDegrees)
        packetBuffer.writeFloat(red)
        packetBuffer.writeFloat(green)
        packetBuffer.writeFloat(blue)
    }

    override fun writeToString(): String {
        return "$offsetDegrees, $red, $green, $blue"
    }

    companion object {
        val CODEC: Codec<FeyParticleData> = RecordCodecBuilder.create {
            it.group(
                Codec.FLOAT.fieldOf("offset_degrees").forGetter(FeyParticleData::offsetDegrees),
                Codec.FLOAT.fieldOf("red").forGetter(FeyParticleData::red),
                Codec.FLOAT.fieldOf("green").forGetter(FeyParticleData::green),
                Codec.FLOAT.fieldOf("blue").forGetter(FeyParticleData::blue)
            ).apply(it, it.stable(Function4 { offsetDegrees, red, green, blue ->
                FeyParticleData(offsetDegrees, red, green, blue)
            }))
        }

        val DESERIALIZER = object : ParticleOptions.Deserializer<FeyParticleData> {
            override fun fromCommand(particleType: ParticleType<FeyParticleData>, stringReader: StringReader): FeyParticleData {
                return FeyParticleData(stringReader.readFloat(), stringReader.readFloat(), stringReader.readFloat(), stringReader.readFloat())
            }

            override fun fromNetwork(particleType: ParticleType<FeyParticleData>, packetBuffer: FriendlyByteBuf): FeyParticleData {
                return FeyParticleData(packetBuffer.readFloat(), packetBuffer.readFloat(), packetBuffer.readFloat(), packetBuffer.readFloat())
            }
        }
    }
}