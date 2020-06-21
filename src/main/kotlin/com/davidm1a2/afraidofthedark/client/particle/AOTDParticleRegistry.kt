package com.davidm1a2.afraidofthedark.client.particle

import net.minecraft.client.Minecraft
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.particles.BasicParticleType
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * An class of AOTD particle types since we don't have a registry system for them yet from forge
 */
object AOTDParticleRegistry {
    // A map of ID -> particle creator. This is used to instantiate the right particle for the id client side
    @OnlyIn(Dist.CLIENT)
    private val PARTICLE_REGISTRY = mapOf(
        ParticleTypes.ENARIA_BASIC_ATTACK_ID to IParticleFactory { _: BasicParticleType?, world: World, x: Double, y: Double, z: Double, _: Double, _: Double, _: Double ->
            ParticleEnariaBasicAttack(world, x, y, z)
        },
        ParticleTypes.ENARIA_SPELL_CAST_ID to IParticleFactory { _: BasicParticleType?, world: World, x: Double, y: Double, z: Double, _: Double, _: Double, _: Double ->
            ParticleEnariaSpellCast(world, x, y, z)
        },
        ParticleTypes.ENARIA_SPELL_CAST_2_ID to IParticleFactory { _: BasicParticleType?, world: World, x: Double, y: Double, z: Double, xSpeed: Double, _: Double, zSpeed: Double ->
            ParticleEnariaSpellCast2(world, x, y, z, xSpeed, zSpeed)
        },
        ParticleTypes.ENARIA_TELEPORT_ID to IParticleFactory { _: BasicParticleType?, world: World, x: Double, y: Double, z: Double, _: Double, _: Double, _: Double ->
            ParticleEnariaTeleport(world, x, y, z)
        },
        ParticleTypes.SPELL_CAST_ID to IParticleFactory { _: BasicParticleType?, world: World, x: Double, y: Double, z: Double, _: Double, _: Double, _: Double ->
            ParticleSpellCast(world, x, y, z)
        },
        ParticleTypes.SPELL_HIT_ID to IParticleFactory { _: BasicParticleType?, world: World, x: Double, y: Double, z: Double, _: Double, _: Double, _: Double ->
            ParticleSpellHit(world, x, y, z)
        },
        ParticleTypes.SMOKE_SCREEN_ID to IParticleFactory { _: BasicParticleType?, world: World, x: Double, y: Double, z: Double, _: Double, _: Double, _: Double ->
            ParticleSmokeScreen(world, x, y, z)
        },
        ParticleTypes.SPELL_LASER to IParticleFactory { _: BasicParticleType?, world: World, x: Double, y: Double, z: Double, _: Double, _: Double, _: Double ->
            ParticleSpellLaser(world, x, y, z)
        },
        ParticleTypes.ENCHANTED_FROG_SPAWN to IParticleFactory { _: BasicParticleType?, world: World, x: Double, y: Double, z: Double, xSpeed: Double, _: Double, zSpeed: Double ->
            ParticleEnchantedFrogSpawn(world, x, y, z, xSpeed, zSpeed)
        },
        ParticleTypes.ENARIAS_ALTAR to IParticleFactory { _: BasicParticleType?, world: World, x: Double, y: Double, z: Double, _: Double, _: Double, _: Double ->
            ParticleEnariasAltar(world, x, y, z)
        }
    )

    /**
     * Called to create a given particle id in the world
     *
     * @param particleId The particle id to create
     * @param world    The world the particle is being created in
     * @param x   The x coordinate of the particle
     * @param y   The y coordinate of the particle
     * @param z   The z coordinate of the particle
     * @param xSpeed   The x speed of the particle
     * @param ySpeed   The y speed of the particle
     * @param zSpeed   The z speed of the particle
     */
    @OnlyIn(Dist.CLIENT)
    fun spawnParticle(
        particleId: ParticleTypes,
        world: World,
        x: Double,
        y: Double,
        z: Double,
        xSpeed: Double,
        ySpeed: Double,
        zSpeed: Double
    ) {
        val particle = PARTICLE_REGISTRY[particleId]?.makeParticle(null, world, x, y, z, xSpeed, ySpeed, zSpeed)
        particle?.let { Minecraft.getInstance().particles.addEffect(it) }
    }

    // Public client and server side enum that can be sent around in packets to notify the clients to spawn particles in
    enum class ParticleTypes {
        ENARIA_BASIC_ATTACK_ID,
        ENARIA_SPELL_CAST_ID,
        ENARIA_SPELL_CAST_2_ID,
        ENARIA_TELEPORT_ID,
        SPELL_CAST_ID,
        SPELL_HIT_ID,
        SMOKE_SCREEN_ID,
        SPELL_LASER,
        ENCHANTED_FROG_SPAWN,
        ENARIAS_ALTAR
    }
}