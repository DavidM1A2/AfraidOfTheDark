package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.mojang.brigadier.StringReader
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.FriendlyByteBuf
import java.util.function.BiFunction

class FlyParticleData(val entityId: Int, val delayTicks: Int) : ParticleOptions {
    override fun getType(): ParticleType<*> {
        return ModParticles.FLY
    }

    override fun writeToNetwork(packetBuffer: FriendlyByteBuf) {
        packetBuffer.writeVarInt(entityId)
        packetBuffer.writeVarInt(delayTicks)
    }

    override fun writeToString(): String {
        return entityId.toString()
    }

    companion object {
        val CODEC: Codec<FlyParticleData> = RecordCodecBuilder.create {
            it.group(
                Codec.INT.fieldOf("entity_id").forGetter(FlyParticleData::entityId),
                Codec.INT.fieldOf("delay_ticks").forGetter(FlyParticleData::delayTicks)
            ).apply(it, it.stable(BiFunction { entityId, delayTicks ->
                FlyParticleData(entityId, delayTicks)
            }))
        }

        val DESERIALIZER = object : ParticleOptions.Deserializer<FlyParticleData> {
            override fun fromCommand(particleType: ParticleType<FlyParticleData>, stringReader: StringReader): FlyParticleData {
                return FlyParticleData(-1, stringReader.readInt())
            }

            override fun fromNetwork(particleType: ParticleType<FlyParticleData>, packetBuffer: FriendlyByteBuf): FlyParticleData {
                return FlyParticleData(packetBuffer.readVarInt(), packetBuffer.readVarInt())
            }
        }
    }
}