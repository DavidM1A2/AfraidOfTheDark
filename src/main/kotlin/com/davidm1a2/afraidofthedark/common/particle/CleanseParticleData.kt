package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.mojang.brigadier.StringReader
import com.mojang.datafixers.util.Function3
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.PacketBuffer
import net.minecraft.particles.IParticleData
import net.minecraft.particles.ParticleType

class CleanseParticleData(val entityId: Int, val offsetDegrees: Float, val radius: Float) : IParticleData {
    override fun getType(): ParticleType<*> {
        return ModParticles.CLEANSE
    }

    override fun writeToNetwork(packetBuffer: PacketBuffer) {
        packetBuffer.writeVarInt(entityId)
        packetBuffer.writeFloat(offsetDegrees)
        packetBuffer.writeFloat(radius)
    }

    override fun writeToString(): String {
        return entityId.toString()
    }

    companion object {
        val CODEC: Codec<CleanseParticleData> = RecordCodecBuilder.create {
            it.group(
                Codec.INT.fieldOf("entity_id").forGetter(CleanseParticleData::entityId),
                Codec.FLOAT.fieldOf("offset_degrees").forGetter(CleanseParticleData::offsetDegrees),
                Codec.FLOAT.fieldOf("radius").forGetter(CleanseParticleData::radius)
            ).apply(it, it.stable(Function3 { entityId, offsetDegrees, radius ->
                CleanseParticleData(entityId, offsetDegrees, radius)
            }))
        }

        val DESERIALIZER = object : IParticleData.IDeserializer<CleanseParticleData> {
            override fun fromCommand(particleType: ParticleType<CleanseParticleData>, stringReader: StringReader): CleanseParticleData {
                return CleanseParticleData(-1, 0f, 1f)
            }

            override fun fromNetwork(particleType: ParticleType<CleanseParticleData>, packetBuffer: PacketBuffer): CleanseParticleData {
                return CleanseParticleData(packetBuffer.readVarInt(), packetBuffer.readFloat(), packetBuffer.readFloat())
            }
        }
    }
}