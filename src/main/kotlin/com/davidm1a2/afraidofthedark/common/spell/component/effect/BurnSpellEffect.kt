package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDDurationSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.block.Blocks
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3d

/**
 * Effect that sets fire to the hit target
 *
 * @constructor adds the editable prop
 */
class BurnSpellEffect : AOTDDurationSpellEffect(ResourceLocation(Constants.MOD_ID, "burn"), ModResearches.ELEMENTAL_MAGIC, 1, 2, 60) {
    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>, reducedParticles: Boolean) {
        if (state.entity != null) {
            val entity = state.entity
            createParticlesAt(5, 10, Vector3d(entity!!.x, entity.y, entity.z), entity.level.dimension(), ModParticles.FIRE)
            entity.remainingFireTicks = entity.remainingFireTicks + getDuration(instance)
        } else {
            val world = state.world
            val position = state.blockPosition
            if (world.isEmptyBlock(position.above())) {
                if (!world.isEmptyBlock(position)) {
                    if (reducedParticles) {
                        createParticlesAround(0, 1, state.position, world.dimension(), ModParticles.FIRE, 0.5)
                    } else {
                        createParticlesAround(2, 4, state.position, world.dimension(), ModParticles.FIRE, 0.5)
                    }
                    world.setBlockAndUpdate(position.above(), Blocks.FIRE.defaultBlockState())
                }
            }
        }
    }

    /**
     * Gets the cost of the delivery method
     *
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return 10.0 + getDuration(instance) * 5.0
    }
}