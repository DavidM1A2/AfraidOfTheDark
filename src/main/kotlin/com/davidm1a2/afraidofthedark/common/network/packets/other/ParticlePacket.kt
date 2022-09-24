package com.davidm1a2.afraidofthedark.common.network.packets.other

import net.minecraft.particles.IParticleData
import net.minecraft.util.math.vector.Vector3d

class ParticlePacket private constructor(
    internal val particles: List<IParticleData>,
    internal val positions: List<Vector3d>,
    internal val speeds: List<Vector3d>
) {
    class Builder internal constructor() {
        private var particles: List<IParticleData>? = null
        private var particle: IParticleData? = null
        private var positions: List<Vector3d>? = null
        private var position: Vector3d? = null
        private var speeds: List<Vector3d>? = null
        private var speed: Vector3d? = null

        fun particle(particle: IParticleData): Builder {
            this.particles = null
            this.particle = particle
            return this
        }

        fun particles(particles: List<IParticleData>): Builder {
            this.particles = particles
            this.particle = null
            return this
        }

        fun position(position: Vector3d): Builder {
            this.positions = null
            this.position = position
            return this
        }

        fun positions(positions: List<Vector3d>): Builder {
            this.positions = positions
            this.position = null
            return this
        }


        fun speed(speed: Vector3d): Builder {
            this.speeds = null
            this.speed = speed
            return this
        }

        fun speeds(speeds: List<Vector3d>): Builder {
            this.speeds = speeds
            this.speed = null
            return this
        }

        fun build(): ParticlePacket {
            return ParticlePacket(
                particles ?: listOf(particle!!),
                positions ?: listOf(position!!),
                speeds ?: speed?.let { listOf(it) } ?: listOf(Vector3d.ZERO))
        }
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }
}