package com.davidm1a2.afraidofthedark.common.network.packets.other

import net.minecraft.core.particles.ParticleOptions
import net.minecraft.world.phys.Vec3

class ParticlePacket private constructor(
    internal val particles: List<ParticleOptions>,
    internal val positions: List<Vec3>,
    internal val speeds: List<Vec3>,
    internal val iterations: Int
) {
    class Builder internal constructor() {
        private var particles: List<ParticleOptions>? = null
        private var particle: ParticleOptions? = null
        private var positions: List<Vec3>? = null
        private var position: Vec3? = null
        private var speeds: List<Vec3>? = null
        private var speed: Vec3? = null
        private var iterations: Int = 1

        fun particle(particle: ParticleOptions): Builder {
            this.particles = null
            this.particle = particle
            return this
        }

        fun particles(particles: List<ParticleOptions>): Builder {
            this.particles = particles
            this.particle = null
            return this
        }

        fun position(position: Vec3): Builder {
            this.positions = null
            this.position = position
            return this
        }

        fun positions(positions: List<Vec3>): Builder {
            this.positions = positions
            this.position = null
            return this
        }


        fun speed(speed: Vec3): Builder {
            this.speeds = null
            this.speed = speed
            return this
        }

        fun speeds(speeds: List<Vec3>): Builder {
            this.speeds = speeds
            this.speed = null
            return this
        }

        fun iterations(iterations: Int): Builder {
            this.iterations = iterations
            return this
        }

        fun build(): ParticlePacket {
            return ParticlePacket(
                particles ?: listOf(particle!!),
                positions ?: listOf(position!!),
                speeds ?: speed?.let { listOf(it) } ?: listOf(Vec3.ZERO),
                iterations)
        }
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }
}