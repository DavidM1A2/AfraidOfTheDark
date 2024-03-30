package com.davidm1a2.afraidofthedark.common.tileEntity.enariasAltar

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDAnimatedTileEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Enaria's altar tile entity which renders the animation
 */
class EnariasAltarTileEntity(blockPos: BlockPos, blockState: BlockState) : AOTDAnimatedTileEntity(
    ModTileEntities.ENARIAS_ALTAR,
    blockPos,
    blockState,
    AnimationHandler(SLOW_CHANNEL, MEDIUM_CHANNEL, FAST_CHANNEL)
) {
    companion object {
        private const val FAST_DISTANCE = 5.0
        private const val MEDIUM_DISTANCE = 15.0

        private val SLOW_CHANNEL = ChannelSpin("SpinSlow", 5f, 60, ChannelMode.LINEAR)
        private val MEDIUM_CHANNEL = ChannelSpin("SpinMedium", 15f, 60, ChannelMode.LINEAR)
        private val FAST_CHANNEL = ChannelSpin("SpinFast", 30f, 60, ChannelMode.LINEAR)

        fun clientTick(level: Level, blockPos: BlockPos, blockState: BlockState, blockEntity: EnariasAltarTileEntity) {
            baseTick(level, blockPos, blockState, blockEntity)
            val animHandler = blockEntity.getAnimationHandler()
            if (animHandler.isAnimationActive("SpinSlow")) {
                // Spawn few particles
                spawnParticlesWithChance(level, blockPos, 0.01)
            } else if (animHandler.isAnimationActive("SpinMedium")) {
                // Spawn a some particles
                spawnParticlesWithChance(level, blockPos, 0.1)
            } else if (animHandler.isAnimationActive("SpinFast")) {
                // Spawn lots of particles
                spawnParticlesWithChance(level, blockPos, 0.6)
            } else {
                // Find out how close the nearest player is, if they're close enough play the right animation
                val closestPlayer = level.getNearestPlayer(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(), MEDIUM_DISTANCE) { true }
                if (closestPlayer != null) {
                    val distance = sqrt(closestPlayer.distanceToSqr(Vec3.atCenterOf(blockPos)))
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

        /**
         * Spawns altar particles around the altar
         *
         * @param chance The chance that the particles will spawn
         */
        private fun spawnParticlesWithChance(level: Level, blockPos: BlockPos, chance: Double) {
            if (Random.nextDouble() < chance) {
                level.addParticle(
                    ModParticles.ENARIAS_ALTAR,
                    false,
                    blockPos.x + 0.2 + Random.nextDouble(0.0, 0.6),
                    blockPos.y + 0.1,
                    blockPos.z + 0.2 + Random.nextDouble(0.0, 0.6),
                    0.0,
                    0.0,
                    0.0
                )
            }
        }
    }
}