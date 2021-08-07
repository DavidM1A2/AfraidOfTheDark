package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.block.IGrowable
import net.minecraft.util.ResourceLocation

/**
 * Spell effect that causes growable blocks to grow
 */
class GrowSpellEffect : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "grow")) {
    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     * @param instance The instance of the effect
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>, reducedParticles: Boolean) {
        val world = state.world
        var position = state.blockPosition
        var blockState = world.getBlockState(position)

        // If we hit a block that crops might be on check the block above and see if we can grow on that instead
        if (blockState.block !is IGrowable) {
            position = position.above()
            blockState = world.getBlockState(position)
        }

        // Grob the block at the current position if it's a type 'IGrowable'
        if (blockState.block is IGrowable) {
            if (reducedParticles) {
                createParticlesAround(1, 3, state.position, world.dimension(), ModParticles.GROW, 2.0)
            } else {
                createParticlesAround(8, 16, state.position, world.dimension(), ModParticles.GROW, 2.0)
            }
            (blockState.block as IGrowable).performBonemeal(world, world.random, position, blockState)
        }
    }

    /**
     * Gets the cost of the delivery method
     *
     * @param instance The instance of the spell effect to grab the cost of
     * @return The cost of the delivery method
     */
    override fun getCost(instance: SpellComponentInstance<SpellEffect>): Double {
        return 15.0
    }
}