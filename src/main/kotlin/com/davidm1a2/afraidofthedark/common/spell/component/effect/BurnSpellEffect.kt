package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDDurationSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3d
import kotlin.math.ceil
import kotlin.math.max
import kotlin.random.Random

/**
 * Effect that sets fire to the hit target
 *
 * @constructor adds the editable prop
 */
class BurnSpellEffect : AOTDDurationSpellEffect("burn", ModResearches.ELEMENTAL_MAGIC, 1.0, 1.0, 60.0) {
    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>) {
        val entity = state.entity
        if (entity != null) {
            val particlePositions = List<Vector3d>(8) {
                entity.position().add(
                    (Random.nextDouble() - 0.5) * entity.boundingBox.xsize * 1.5,
                    (Random.nextDouble() - 0.5) * entity.boundingBox.ysize * 0.8,
                    (Random.nextDouble() - 0.5) * entity.boundingBox.zsize * 1.5
                )
            }
            createParticlesAt(
                state, ParticlePacket.builder()
                    .particle(ModParticles.FIRE)
                    .positions(particlePositions)
                    .speed(
                        Vector3d(
                            Random.nextDouble() * 0.01 - 0.005,
                            0.1,
                            Random.nextDouble() * 0.01 - 0.005
                        )
                    )
                    .build()
            )
            entity.remainingFireTicks = max(entity.remainingFireTicks, ceil(getDuration(instance) * 20).toInt())
        } else {
            val world = state.world
            val reverseHitDir = state.direction.reverse()
            val position = BlockPos(state.position.add(reverseHitDir.scale(0.01)))
            var setBlockOnFire = false
            if (world.isEmptyBlock(position)) {
                if (!world.isEmptyBlock(position.below())) {
                    createParticlesAt(
                        state, ParticlePacket.builder()
                            .particle(ModParticles.FIRE)
                            .position(state.position)
                            .speed(
                                Vector3d(
                                    Random.nextDouble() * 0.01 - 0.005,
                                    0.1,
                                    Random.nextDouble() * 0.01 - 0.005
                                )
                            )
                            .build()
                    )
                    world.setBlockAndUpdate(position, Blocks.FIRE.defaultBlockState())
                    setBlockOnFire = true
                }
            }
            if (!setBlockOnFire) {
                createFizzleParticleAt(state)
            }
        }
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        // If burning an entity, add 3.0 vitae per second
        return getDuration(instance) * 2.0 - 1.0    // Burning a block only costs 1, first tick of entity burn is half off
    }
}