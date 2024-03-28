package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.mojang.brigadier.StringReader
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.FriendlyByteBuf
import java.util.function.BiFunction

class ArrowTrailParticleData(val entityId: Int, val delayTicks: Int) : ParticleOptions {
    override fun getType(): ParticleType<*> {
        return ModParticles.ARROW_TRAIL
    }

    override fun writeToNetwork(packetBuffer: FriendlyByteBuf) {
        packetBuffer.writeVarInt(entityId)
        packetBuffer.writeVarInt(delayTicks)
    }

    override fun writeToString(): String {
        return entityId.toString()
    }

    companion object {
        val CODEC: Codec<ArrowTrailParticleData> = RecordCodecBuilder.create {
            it.group(
                Codec.INT.fieldOf("entity_id").forGetter(ArrowTrailParticleData::entityId),
                Codec.INT.fieldOf("delay_ticks").forGetter(ArrowTrailParticleData::delayTicks)
            ).apply(it, it.stable(BiFunction { entityId, delayTicks ->
                ArrowTrailParticleData(entityId, delayTicks)
            }))
        }

        val DESERIALIZER = object : ParticleOptions.Deserializer<ArrowTrailParticleData> {
            override fun fromCommand(particleType: ParticleType<ArrowTrailParticleData>, stringReader: StringReader): ArrowTrailParticleData {
                return ArrowTrailParticleData(-1, 0)
            }

            override fun fromNetwork(particleType: ParticleType<ArrowTrailParticleData>, packetBuffer: FriendlyByteBuf): ArrowTrailParticleData {
                return ArrowTrailParticleData(packetBuffer.readVarInt(), packetBuffer.readVarInt())
            }
        }
    }
}