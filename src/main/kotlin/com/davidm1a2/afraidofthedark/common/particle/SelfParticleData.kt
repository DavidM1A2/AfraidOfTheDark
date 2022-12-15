package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.mojang.brigadier.StringReader
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.PacketBuffer
import net.minecraft.particles.IParticleData
import net.minecraft.particles.ParticleType
import java.util.function.BiFunction

class SelfParticleData(val entityId: Int, val offsetDegrees: Float) : IParticleData {
    override fun getType(): ParticleType<*> {
        return ModParticles.SELF
    }

    override fun writeToNetwork(packetBuffer: PacketBuffer) {
        packetBuffer.writeVarInt(entityId)
        packetBuffer.writeFloat(offsetDegrees)
    }

    override fun writeToString(): String {
        return entityId.toString()
    }

    companion object {
        val CODEC: Codec<SelfParticleData> = RecordCodecBuilder.create {
            it.group(
                Codec.INT.fieldOf("entity_id").forGetter(SelfParticleData::entityId),
                Codec.FLOAT.fieldOf("offset_degrees").forGetter(SelfParticleData::offsetDegrees)
            ).apply(it, it.stable(BiFunction { entityId, offsetDegrees ->
                SelfParticleData(entityId, offsetDegrees)
            }))
        }

        val DESERIALIZER = object : IParticleData.IDeserializer<SelfParticleData> {
            override fun fromCommand(particleType: ParticleType<SelfParticleData>, stringReader: StringReader): SelfParticleData {
                return SelfParticleData(-1, 0f)
            }

            override fun fromNetwork(particleType: ParticleType<SelfParticleData>, packetBuffer: PacketBuffer): SelfParticleData {
                return SelfParticleData(packetBuffer.readVarInt(), packetBuffer.readFloat())
            }
        }
    }
}