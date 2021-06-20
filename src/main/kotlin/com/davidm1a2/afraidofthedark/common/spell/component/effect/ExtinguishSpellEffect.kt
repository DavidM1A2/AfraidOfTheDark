package com.davidm1a2.afraidofthedark.common.spell.component.effect

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.block.Blocks
import net.minecraft.block.FireBlock
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

/**
 * Effect that extinguishes fire
 */
class ExtinguishSpellEffect : AOTDSpellEffect(ResourceLocation(Constants.MOD_ID, "extinguish")) {
    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    override fun procEffect(state: DeliveryTransitionState, instance: SpellComponentInstance<SpellEffect>) {
        // If we hit an entity extinguish them
        val entity = state.getEntity()
        if (entity != null) {
            createParticlesAt(3, 5, Vec3d(entity.posX, entity.posY, entity.posZ), entity.dimension, ModParticles.FIRE)
            entity.extinguish()
        } else {
            val world: World = state.world
            val position = state.blockPosition
            if (world.getBlockState(position).block is FireBlock) {
                createParticlesAt(1, 3, state.position, world.dimension.type, ModParticles.FIRE)
                world.setBlockState(position, Blocks.AIR.defaultState)
            }
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