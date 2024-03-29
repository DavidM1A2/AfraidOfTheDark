package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.mojang.brigadier.StringReader
import com.mojang.datafixers.util.Function4
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.PacketBuffer
import net.minecraft.particles.IParticleData
import net.minecraft.particles.ParticleType

class ShieldParticleData(val entityId: Int, val offsetDegrees: Float, val radius: Float, val duration: Int) : IParticleData {
    override fun getType(): ParticleType<*> {
        return ModParticles.SHIELD
    }

    override fun writeToNetwork(packetBuffer: PacketBuffer) {
        packetBuffer.writeVarInt(entityId)
        packetBuffer.writeFloat(offsetDegrees)
        packetBuffer.writeFloat(radius)
        packetBuffer.writeVarInt(duration)
    }

    override fun writeToString(): String {
        return entityId.toString()
    }

    companion object {
        val CODEC: Codec<ShieldParticleData> = RecordCodecBuilder.create {
            it.group(
                Codec.INT.fieldOf("entity_id").forGetter(ShieldParticleData::entityId),
                Codec.FLOAT.fieldOf("offset_degrees").forGetter(ShieldParticleData::offsetDegrees),
                Codec.FLOAT.fieldOf("radius").forGetter(ShieldParticleData::radius),
                Codec.INT.fieldOf("duration").forGetter(ShieldParticleData::duration)
            ).apply(it, it.stable(Function4 { entityId, offsetDegrees, radius, duration ->
                ShieldParticleData(entityId, offsetDegrees, radius, duration)
            }))
        }

        val DESERIALIZER = object : IParticleData.IDeserializer<ShieldParticleData> {
            override fun fromCommand(particleType: ParticleType<ShieldParticleData>, stringReader: StringReader): ShieldParticleData {
                return ShieldParticleData(-1, 0f, 1f, 20)
            }

            override fun fromNetwork(particleType: ParticleType<ShieldParticleData>, packetBuffer: PacketBuffer): ShieldParticleData {
                return ShieldParticleData(packetBuffer.readVarInt(), packetBuffer.readFloat(), packetBuffer.readFloat(), packetBuffer.readVarInt())
            }
        }
    }
}