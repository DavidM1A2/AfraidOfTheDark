package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.mojang.brigadier.StringReader
import com.mojang.datafixers.util.Function3
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.PacketBuffer
import net.minecraft.particles.IParticleData
import net.minecraft.particles.ParticleType

class FeedParticleData(val entityId: Int, val offsetDegrees: Float, val radius: Float) : IParticleData {
    override fun getType(): ParticleType<*> {
        return ModParticles.FEED
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
        val CODEC: Codec<FeedParticleData> = RecordCodecBuilder.create {
            it.group(
                Codec.INT.fieldOf("entity_id").forGetter(FeedParticleData::entityId),
                Codec.FLOAT.fieldOf("offset_degrees").forGetter(FeedParticleData::offsetDegrees),
                Codec.FLOAT.fieldOf("radius").forGetter(FeedParticleData::radius)
            ).apply(it, it.stable(Function3 { entityId, offsetDegrees, radius ->
                FeedParticleData(entityId, offsetDegrees, radius)
            }))
        }

        val DESERIALIZER = object : IParticleData.IDeserializer<FeedParticleData> {
            override fun fromCommand(particleType: ParticleType<FeedParticleData>, stringReader: StringReader): FeedParticleData {
                return FeedParticleData(-1, 0f, 1f)
            }

            override fun fromNetwork(particleType: ParticleType<FeedParticleData>, packetBuffer: PacketBuffer): FeedParticleData {
                return FeedParticleData(packetBuffer.readVarInt(), packetBuffer.readFloat(), packetBuffer.readFloat())
            }
        }
    }
}