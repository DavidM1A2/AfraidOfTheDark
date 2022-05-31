package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDDurationSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.block.Blocks
import net.minecraft.util.math.vector.Vector3d

/**
 * Effect that sets fire to the hit target
 *
 * @constructor adds the editable prop
 */
class BurnSpellEffect : AOTDDurationSpellEffect("burn", ModResearches.ELEMENTAL_MAGIC, 1, 1, 60) {
    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>) {
        if (state.entity != null) {
            val entity = state.entity
            createParticlesAt(5, 10, Vector3d(entity!!.x, entity.y, entity.z), entity.level.dimension(), ModParticles.FIRE)
            entity.remainingFireTicks = entity.remainingFireTicks + getDuration(instance) * 20
        } else {
            val world = state.world
            val position = state.blockPosition
            if (world.isEmptyBlock(position.above())) {
                if (!world.isEmptyBlock(position)) {
                    createParticlesAround(2, 4, state.position, world.dimension(), ModParticles.FIRE, 0.5)
                    world.setBlockAndUpdate(position.above(), Blocks.FIRE.defaultBlockState())
                }
            }
        }
    }

    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        // If burning an entity, add 3.0 vitae per second
        return getDuration(instance) * 2.0 - 1.0    // Burning a block only costs 1, first tick of entity burn is half off
    }
}