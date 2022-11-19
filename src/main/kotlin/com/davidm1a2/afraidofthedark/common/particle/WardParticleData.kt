package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.mojang.brigadier.StringReader
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.PacketBuffer
import net.minecraft.particles.IParticleData
import net.minecraft.particles.ParticleType
import net.minecraft.util.Direction
import java.util.function.BiFunction

class WardParticleData(val entityId: Int, val direction: Direction) : IParticleData {
    constructor(direction: Direction) : this(-1, direction)

    override fun getType(): ParticleType<*> {
        return ModParticles.WARD
    }

    override fun writeToNetwork(packetBuffer: PacketBuffer) {
        packetBuffer.writeVarInt(entityId)
        packetBuffer.writeVarInt(direction.ordinal)
    }

    override fun writeToString(): String {
        return entityId.toString()
    }

    companion object {
        val CODEC: Codec<WardParticleData> = RecordCodecBuilder.create {
            it.group(
                Codec.INT.fieldOf("entity_id").forGetter(WardParticleData::entityId),
                Codec.INT.fieldOf("direction").xmap({ int -> Direction.values()[int] }, { dir -> dir.ordinal }).forGetter(WardParticleData::direction)
            ).apply(it, it.stable(BiFunction { entityId, direction ->
                WardParticleData(entityId, direction)
            }))
        }

        val DESERIALIZER = object : IParticleData.IDeserializer<WardParticleData> {
            override fun fromCommand(particleType: ParticleType<WardParticleData>, stringReader: StringReader): WardParticleData {
                return WardParticleData(-1, Direction.UP)
            }

            override fun fromNetwork(particleType: ParticleType<WardParticleData>, packetBuffer: PacketBuffer): WardParticleData {
                return WardParticleData(packetBuffer.readVarInt(), Direction.values()[packetBuffer.readVarInt()])
            }
        }
    }
}