package com.davidm1a2.afraidofthedark.common.tileEntity.enariasAltar

import com.davidm1a2.afraidofthedark.client.particle.AOTDParticleRegistry
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDAnimatedTileEntity
import kotlin.random.Random

/**
 * Enaria's altar tile entity which renders the animation
 */
class TileEntityEnariasAltar : AOTDAnimatedTileEntity(
    ModTileEntities.ENARIAS_ALTAR,
    AnimationHandler(
        ChannelSpin("SpinSlow", 5f, 60, ChannelMode.LINEAR),
        ChannelSpin("SpinMedium", 15f, 60, ChannelMode.LINEAR),
        ChannelSpin("SpinFast", 30f, 60, ChannelMode.LINEAR)
    )
) {
    /**
     * Called every tick to update the tile entity's state
     */
    override fun tick() {
        super.tick()
        if (world.isRemote) {
            val animHandler = getAnimationHandler()
            if (animHandler.isAnimationActive("SpinSlow")) {
                // Spawn few particles
                spawnParticlesWithChance(0.01)
            } else if (animHandler.isAnimationActive("SpinMedium")) {
                // Spawn a some particles
                spawnParticlesWithChance(0.1)
            } else if (animHandler.isAnimationActive("SpinFast")) {
                // Spawn lots of particles
                spawnParticlesWithChance(0.6)
            } else {
                // Find out how close the nearest player is, if they're close enough play the right animation
                val closestPlayer = world.getClosestPlayer(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), MEDIUM_DISTANCE) { true }
                if (closestPlayer != null) {
                    val distance = closestPlayer.getDistance(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
                    if (distance <= FAST_DISTANCE) {
                        animHandler.playAnimation("SpinFast")
                    } else {
                        animHandler.playAnimation("SpinMedium")
                    }
                } else {
                    animHandler.playAnimation("SpinSlow")
                }
            }
        }
    }

    /**
     * Spawns altar particles around the altar
     *
     * @param chance The chance that the particles will spawn
     */
    private fun spawnParticlesWithChance(chance: Double) {
        if (Random.nextDouble() < chance) {
            AOTDParticleRegistry.spawnParticle(
                AOTDParticleRegistry.ParticleTypes.ENARIAS_ALTAR,
                world,
                pos.x + 0.2 + Random.nextDouble(0.0, 0.6),
                pos.y + 0.1,
                pos.z + 0.2 + Random.nextDouble(0.0, 0.6),
                0.0,
                0.0,
                0.0
            )
        }
    }

    companion object {
        private const val FAST_DISTANCE = 5.0
        private const val MEDIUM_DISTANCE = 15.0
    }
}