package com.davidm1a2.afraidofthedark.common.particle

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.mojang.brigadier.StringReader
import com.mojang.datafixers.util.Function4
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.network.FriendlyByteBuf

class ProjectileParticleData(val scale: Float, val red: Float, val green: Float, val blue: Float) : ParticleOptions {
    override fun getType(): ParticleType<*> {
        return ModParticles.PROJECTILE
    }

    override fun writeToNetwork(packetBuffer: FriendlyByteBuf) {
        packetBuffer.writeFloat(scale)
        packetBuffer.writeFloat(red)
        packetBuffer.writeFloat(green)
        packetBuffer.writeFloat(blue)
    }

    override fun writeToString(): String {
        return "$scale, $red, $green, $blue"
    }

    companion object {
        val CODEC: Codec<ProjectileParticleData> = RecordCodecBuilder.create {
            it.group(
                Codec.FLOAT.fieldOf("scale").forGetter(ProjectileParticleData::scale),
                Codec.FLOAT.fieldOf("red").forGetter(ProjectileParticleData::red),
                Codec.FLOAT.fieldOf("green").forGetter(ProjectileParticleData::green),
                Codec.FLOAT.fieldOf("blue").forGetter(ProjectileParticleData::blue)
            ).apply(it, it.stable(Function4 { scale, red, green, blue ->
                ProjectileParticleData(scale, red, green, blue)
            }))
        }

        val DESERIALIZER = object : ParticleOptions.Deserializer<ProjectileParticleData> {
            override fun fromCommand(particleType: ParticleType<ProjectileParticleData>, stringReader: StringReader): ProjectileParticleData {
                return ProjectileParticleData(stringReader.readFloat(), stringReader.readFloat(), stringReader.readFloat(), stringReader.readFloat())
            }

            override fun fromNetwork(particleType: ParticleType<ProjectileParticleData>, packetBuffer: FriendlyByteBuf): ProjectileParticleData {
                return ProjectileParticleData(packetBuffer.readFloat(), packetBuffer.readFloat(), packetBuffer.readFloat(), packetBuffer.readFloat())
            }
        }
    }
}