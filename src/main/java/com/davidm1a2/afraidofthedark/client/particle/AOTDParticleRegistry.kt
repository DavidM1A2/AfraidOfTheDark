package com.davidm1a2.afraidofthedark.client.particle

import net.minecraft.client.Minecraft
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * An class of AOTD particle types since we don't have a registry system for them yet from forge
 */
object AOTDParticleRegistry {
    // A map of ID -> particle creator. This is used to instantiate the right particle for the id client side
    @SideOnly(Side.CLIENT)
    private val PARTICLE_REGISTRY = mapOf(
        ParticleTypes.ENARIA_BASIC_ATTACK_ID to IParticleFactory { _: Int, world: World, x: Double, y: Double, z: Double, _: Double, _: Double, _: Double, _: IntArray? ->
            ParticleEnariaBasicAttack(
                world,
                x,
                y,
                z
            )
        },
        ParticleTypes.ENARIA_SPELL_CAST_ID to IParticleFactory { _: Int, world: World, x: Double, y: Double, z: Double, _: Double, _: Double, _: Double, _: IntArray? ->
            ParticleEnariaSpellCast(
                world,
                x,
                y,
                z
            )
        },
        ParticleTypes.ENARIA_SPELL_CAST_2_ID to IParticleFactory { _: Int, world: World, x: Double, y: Double, z: Double, xSpeed: Double, _: Double, zSpeed: Double, _: IntArray? ->
            ParticleEnariaSpellCast2(
                world,
                x,
                y,
                z,
                xSpeed,
                zSpeed
            )
        },
        ParticleTypes.ENARIA_TELEPORT_ID to IParticleFactory { _: Int, world: World, x: Double, y: Double, z: Double, _: Double, _: Double, _: Double, _: IntArray? ->
            ParticleEnariaTeleport(
                world,
                x,
                y,
                z
            )
        },
        ParticleTypes.SPELL_CAST_ID to IParticleFactory { _: Int, world: World, x: Double, y: Double, z: Double, _: Double, _: Double, _: Double, _: IntArray? ->
            ParticleSpellCast(
                world,
                x,
                y,
                z
            )
        },
        ParticleTypes.SPELL_HIT_ID to IParticleFactory { _: Int, world: World, x: Double, y: Double, z: Double, _: Double, _: Double, _: Double, _: IntArray? ->
            ParticleSpellHit(
                world,
                x,
                y,
                z
            )
        },
        ParticleTypes.SMOKE_SCREEN_ID to IParticleFactory { _: Int, world: World, x: Double, y: Double, z: Double, _: Double, _: Double, _: Double, _: IntArray? ->
            ParticleSmokeScreen(
                world,
                x,
                y,
                z
            )
        },
        ParticleTypes.SPELL_LASER to IParticleFactory { _: Int, world: World, x: Double, y: Double, z: Double, _: Double, _: Double, _: Double, _: IntArray? ->
            ParticleSpellLaser(
                world,
                x,
                y,
                z
            )
        },
        ParticleTypes.ENCHANTED_FROG_SPAWN to IParticleFactory { _: Int, world: World, x: Double, y: Double, z: Double, xSpeed: Double, _: Double, zSpeed: Double, _: IntArray? ->
            ParticleEnchantedFrogSpawn(
                world,
                x,
                y,
                z,
                xSpeed,
                zSpeed
            )
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
    @SideOnly(Side.CLIENT)
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
        val particle = PARTICLE_REGISTRY[particleId]?.createParticle(0, world, x, y, z, xSpeed, ySpeed, zSpeed)
        particle?.let { Minecraft.getMinecraft().effectRenderer.addEffect(it) }
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
        ENCHANTED_FROG_SPAWN
    }
}