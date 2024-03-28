package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.ProcResult
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FireBlock

/**
 * Effect that extinguishes fire
 */
class ExtinguishSpellEffect : AOTDSpellEffect("extinguish", ModResearches.ELEMENTAL_MAGIC) {
    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>): ProcResult {
        // If we hit an entity extinguish them
        val entity = state.entity
        if (entity != null) {
            if (entity.isOnFire) {
                val width = entity.bbWidth.toDouble()
                val height = entity.bbHeight.toDouble()
                entity.clearFire()
                state.world.sendParticles(
                    ParticleTypes.LARGE_SMOKE,
                    entity.x,
                    entity.y + height / 2,
                    entity.z,
                    // Spawn 6 particles
                    6,
                    // Velocity is used as an offset for the particle
                    width / 2,
                    height / 2,
                    width / 2,
                    0.0
                )
            } else {
                return ProcResult.failure()
            }
        } else {
            val world = state.world
            val reverseHitDir = state.direction.reverse()
            val position = BlockPos(state.position.add(reverseHitDir.scale(0.01)))
            if (world.getBlockState(position).block is FireBlock) {
                world.setBlockAndUpdate(position, Blocks.AIR.defaultBlockState())
                state.world.sendParticles(
                    ParticleTypes.LARGE_SMOKE,
                    // Randomize the position within the block
                    position.x + 0.5,
                    position.y + 0.5,
                    position.z + 0.5,
                    // Spawn two particles
                    2,
                    // Velocity is used as an offset for the particle
                    0.5,
                    0.5,
                    0.5,
                    0.0
                )
            } else {
                return ProcResult.failure()
            }
        }
        return ProcResult.success()
    }

    /**
     * Gets the cost of the delivery method
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return 0.2
    }
}