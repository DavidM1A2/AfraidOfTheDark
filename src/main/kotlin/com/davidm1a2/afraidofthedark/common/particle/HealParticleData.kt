package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.mojang.brigadier.StringReader
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.PacketBuffer
import net.minecraft.particles.IParticleData
import net.minecraft.particles.ParticleType
import java.util.function.BiFunction

class HealParticleData(val entityId: Int, val offsetDegrees: Float) : IParticleData {
    override fun getType(): ParticleType<*> {
        return ModParticles.HEAL
    }

    override fun writeToNetwork(packetBuffer: PacketBuffer) {
        packetBuffer.writeVarInt(entityId)
        packetBuffer.writeFloat(offsetDegrees)
    }

    override fun writeToString(): String {
        return entityId.toString()
    }

    companion object {
        val CODEC: Codec<HealParticleData> = RecordCodecBuilder.create {
            it.group(
                Codec.INT.fieldOf("entity_id").forGetter(HealParticleData::entityId),
                Codec.FLOAT.fieldOf("offset_degrees").forGetter(HealParticleData::offsetDegrees)
            ).apply(it, it.stable(BiFunction { entityId, offsetDegrees ->
                HealParticleData(entityId, offsetDegrees)
            }))
        }

        val DESERIALIZER = object : IParticleData.IDeserializer<HealParticleData> {
            override fun fromCommand(particleType: ParticleType<HealParticleData>, stringReader: StringReader): HealParticleData {
                return HealParticleData(-1, 0f)
            }

            override fun fromNetwork(particleType: ParticleType<HealParticleData>, packetBuffer: PacketBuffer): HealParticleData {
                return HealParticleData(packetBuffer.readVarInt(), packetBuffer.readFloat())
            }
        }
    }
}